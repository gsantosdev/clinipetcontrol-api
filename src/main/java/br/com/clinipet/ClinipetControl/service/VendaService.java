package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.controller.dto.request.VendaDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.AgendamentoMapper;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.ItemVenda;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.Venda;
import br.com.clinipet.ClinipetControl.model.enums.StatusVendaEnum;
import br.com.clinipet.ClinipetControl.model.enums.TipoLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;

    private final ClienteService clienteService;

    private final UsuarioService usuarioService;

    private final LancamentoService lancamentoService;

    private final AgendamentoService agendamentoService;

    private final AgendamentoMapper agendamentoMapper;

    @Transactional
    public Venda efetuarVenda(VendaDTO vendaDTO) {


        List<ItemVenda> itemList = new ArrayList<>();

        vendaDTO.getItensVenda().forEach(itemVendaDTO -> {

            Agendamento agendamento = agendamentoMapper.toEntity(itemVendaDTO.getAgendamento());
            Agendamento agendamentoSalvo = agendamentoService.marcar(agendamento);
            itemList.add(ItemVenda.builder().agendamento(agendamentoSalvo).quantidade(itemVendaDTO.getQuantidade()).build());
        });

        Venda venda = Venda.builder().itensVenda(itemList).dataCriacao(Date.valueOf(LocalDate.now())).status(StatusVendaEnum.PENDENTE).build();

        venda.getItensVenda().forEach(itemVenda -> itemVenda.setVenda(venda));

        Cliente clienteVenda = Optional.ofNullable(vendaDTO.getIdCliente())
                .flatMap(clienteService::obterPorId)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!"));

        venda.setCliente(clienteVenda);

        vendaRepository.save(venda);



        lancamentoService.salvar(Lancamento.builder()
                .descricao("Venda de serviço")
                .usuario(usuarioService.obterPorId(vendaDTO.getIdUsuario()).orElseThrow(() -> new RegraNegocioException("Usuario não encontrado.")))
                .tipo(TipoLancamentoEnum.RECEITA)
                .dataCriacao(Date.valueOf(LocalDate.now()))
                .valor(BigDecimal.valueOf(venda.getValorTotal()))
                .build());

        return venda;
    }


    public Venda obterVenda(Long id) {
        return vendaRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Venda não encontrada"));
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public List<Venda> listarTodasPorIdCliente(Long id) {

        return clienteService.obterPorId(id)
                .map(vendaRepository::findAllByCliente)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!"));
    }

}
