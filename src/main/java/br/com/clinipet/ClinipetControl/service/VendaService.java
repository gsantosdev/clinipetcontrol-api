package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.controller.dto.request.VendaDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.AgendamentoMapper;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.ItemVenda;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.model.entity.Venda;
import br.com.clinipet.ClinipetControl.model.entity.dao.ordemDeServicoDAO;
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

    private final ServicoService servicoService;

    private final LancamentoService lancamentoService;

    private final AgendamentoService agendamentoService;

    private final AgendamentoMapper agendamentoMapper;

    @Transactional
    public Venda efetuarVendaServico(VendaDTO vendaDTO) {


        List<ItemVenda> itemList = new ArrayList<>();


        //Cria venda de serviço
        vendaDTO.getItensVenda().forEach(itemVendaDTO -> {

            Agendamento agendamento = agendamentoMapper.toEntity(itemVendaDTO.getAgendamento());
            Agendamento agendamentoSalvo = agendamentoService.marcar(agendamento);
            itemList.add(ItemVenda.builder().agendamento(agendamentoSalvo).quantidade(itemVendaDTO.getQuantidade()).build());
        });

        Venda venda = Venda.builder().tipo("servico").itensVenda(itemList).dataCriacao(Date.valueOf(LocalDate.now())).build();

        venda.getItensVenda().forEach(itemVenda -> itemVenda.setVenda(venda));

        Cliente clienteVenda = Optional.ofNullable(vendaDTO.getIdCliente())
                .flatMap(clienteService::obterPorId)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!"));

        venda.setCliente(clienteVenda);

        vendaRepository.save(venda);

        //Achar serviço
        StringBuilder servicos = new StringBuilder();
        List<Servico> servicoList = new ArrayList<>();

        vendaDTO.getItensVenda()
                .forEach(itemVendaDTO -> servicoService
                        .obterPorId(itemVendaDTO.getAgendamento().getIdServico())
                        .map(servicoList::add)
                        .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado!")));


        //Adiciona nome dos serviços na ordem de serviço
        servicoList.forEach(servico -> {
            if (servicos.isEmpty()) {
                servicos.append(servico.getNome());
            } else {
                servicos.append(" + ").append(servico.getNome());
            }
        });

        //Salva o lançamento da venda efetuada
        lancamentoService.salvar(Lancamento.builder()
                .descricao(servicos.toString())
                .venda(venda)
                .usuario(usuarioService.obterPorId(vendaDTO.getIdUsuario()).orElseThrow(() -> new RegraNegocioException("Usuario não encontrado.")))
                .tipo(TipoLancamentoEnum.RECEITA)
                .dataCriacao(Date.valueOf(LocalDate.now()))
                .valor(BigDecimal.valueOf(venda.getValorTotal()))
                .build());

        return venda;
    }

    public List<ItemVenda> obterItensDaVenda(Long id) {
        return obterVenda(id).getItensVenda();
    }

    public Venda obterVenda(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Venda não encontrada"));
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public List<Venda> listarTodasPorIdCliente(Long id) {

        return clienteService.obterPorId(id)
                .map(vendaRepository::findAllByCliente)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!"));
    }

    public List<ordemDeServicoDAO> listarOrdensPorCliente(String busca){
        return vendaRepository.findOrdensByCliente(busca);
    }


}
