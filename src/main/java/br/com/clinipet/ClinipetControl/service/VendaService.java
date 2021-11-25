package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.controller.dto.request.VendaDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.AgendamentoMapper;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.ItemVenda;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.Produto;
import br.com.clinipet.ClinipetControl.model.entity.Venda;
import br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO;
import br.com.clinipet.ClinipetControl.model.entity.dao.ordemDeServicoDAO;
import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.enums.TipoLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;

    private final ClienteService clienteService;

    private final AnimalService animalService;

    private final UsuarioService usuarioService;

    private final ServicoService servicoService;

    private final ProdutoService produtoService;

    private final LancamentoService lancamentoService;

    private final AgendamentoService agendamentoService;

    private final AgendamentoMapper agendamentoMapper;


    @Transactional
    public Venda efetuarVendaProduto(VendaDTO vendaDTO) {

        List<ItemVenda> itemList = new ArrayList<>();

        //Cria venda de produto e ja faz baixa no estoque
        vendaDTO.getItensVenda().forEach(itemVendaDTO -> {

            Produto produto = produtoService.obterPorId(itemVendaDTO.getIdProduto()).orElseThrow(() -> new RegraNegocioException("O produto não existe!"));
            produtoService.baixaEstoque(produto.getId(), itemVendaDTO.getQuantidade());
            itemList.add(ItemVenda.builder().produto(produto).quantidade(itemVendaDTO.getQuantidade()).build());
        });

        Venda venda = Venda.builder().tipo("produto").cpfCnpj(vendaDTO.getCpfCnpj()).itensVenda(itemList).dataCriacao(LocalDateTime.now()).build();

        venda.getItensVenda().forEach(itemVenda -> itemVenda.setVenda(venda));


        Venda vendaSalva = vendaRepository.save(venda);


        //Baixa no estoque


        //Salva o lançamento da venda efetuada
        lancamentoService.salvar(Lancamento.builder()
                .descricao("Venda de produto")
                .venda(vendaSalva)
                .status(StatusLancamentoEnum.AGUARDANDO_PAGAMENTO)
                .usuario(usuarioService.obterPorId(vendaDTO.getIdUsuario()).orElseThrow(() -> new RegraNegocioException("Usuario não encontrado.")))
                .tipo(TipoLancamentoEnum.RECEITA)
                .dataExecucao(new Date(System.currentTimeMillis()))
                .valor(BigDecimal.valueOf(venda.getValorTotal()))
                .cpfCnpj(vendaSalva.getCpfCnpj())
                .build());

        return vendaSalva;
    }

    @Transactional
    public Venda efetuarVendaServico(VendaDTO vendaDTO) {

        List<ItemVenda> itemList = new ArrayList<>();

        //Cria venda de serviço
        vendaDTO.getItensVenda().forEach(itemVendaDTO -> {

            Agendamento agendamento = agendamentoMapper.toEntity(itemVendaDTO.getAgendamento());
            Agendamento agendamentoSalvo = agendamentoService.marcar(agendamento);

            itemList.add(ItemVenda.builder().agendamento(agendamentoSalvo).quantidade(itemVendaDTO.getQuantidade()).build());
        });

        Venda venda = Venda.builder().tipo("servico").itensVenda(itemList).dataCriacao(LocalDateTime.now()).build();

        venda.getItensVenda().forEach(itemVenda -> itemVenda.setVenda(venda));

        Cliente clienteVenda = Optional.ofNullable(vendaDTO.getIdCliente())
                .flatMap(clienteService::obterPorId)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!"));

        venda.setCliente(clienteVenda);

        vendaRepository.save(venda);

        //Cria um lançamento para cada serviço
        vendaDTO.getItensVenda()
                .forEach(itemVendaDTO -> servicoService
                        .obterPorId(Objects.requireNonNull(itemVendaDTO.getAgendamento()).getIdServico())
                        .map(servico -> lancamentoService.salvar(Lancamento.builder()
                                .descricao(servico.getNome() + " - " + animalService.obterPorId(itemVendaDTO.getAgendamento()
                                        .getIdAnimal()).map(Animal::getNome).orElseThrow(() -> new RegraNegocioException("Animal não encontrado.")))
                                .venda(venda).usuario(usuarioService.obterPorId(vendaDTO.getIdUsuario())
                                        .orElseThrow(() -> new RegraNegocioException("Usuario não encontrado.")))
                                .tipo(TipoLancamentoEnum.RECEITA)
                                .dataExecucao(itemVendaDTO.getAgendamento().getDataHorario())
                                .valor(BigDecimal.valueOf(servico.getValorItem()))
                                .cpfCnpj(clienteService.obterPorId(vendaDTO.getIdCliente()).orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!")).getCpf())
                                .build()))
                        .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado!")));

        /*
        servicoList.forEach(servico -> lancamentoService.salvar(Lancamento.builder()
                .descricao(servico.getNome())
                .venda(venda).usuario(usuarioService.obterPorId(vendaDTO.getIdUsuario())
                        .orElseThrow(() -> new RegraNegocioException("Usuario não encontrado.")))
                .tipo(TipoLancamentoEnum.RECEITA)
                .dataExecucao(servico.get)
                .valor(BigDecimal.valueOf(servico.getValorItem()))
                .build())
        );
        */
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

    public List<ordemDeServicoDAO> listarOrdensPorCliente(String busca) {


        List<ordemDeServicoDAO> ordensByCliente = vendaRepository.findOrdensByCliente(busca);


        ordensByCliente
                .forEach(ordemDeServicoDAO -> ordemDeServicoDAO.setAgendamento(lancamentoService
                        .obterPorId(ordemDeServicoDAO.getIdLancamento())
                        .map(Lancamento::getVenda)
                        .map(Venda::getItensVenda)
                        .get()
                        .stream()
                        .findFirst()
                        .map(ItemVenda::getAgendamento)
                        .map(agendamentoMapper::toDTO)
                        .orElseThrow(() -> new RegraNegocioException("Falha ao achar o agendamento"))));

        return ordensByCliente;

    }


}
