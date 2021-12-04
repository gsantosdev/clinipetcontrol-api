package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.controller.dto.request.LancamentoIdsDTO;
import br.com.clinipet.ClinipetControl.controller.dto.response.FechamentoCaixaResponseDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO;
import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.enums.TipoLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    private final AgendamentoService agendamentoService;

    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        if (lancamento.getDataExecucao() == null) {
            lancamento.setDataExecucao(new Date(System.currentTimeMillis()));
        }
        if (lancamento.getStatus() == null) {
            lancamento.setStatus(StatusLancamentoEnum.PENDENTE);
        }
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getIdLancamento());
        if ((lancamento.getStatus() == StatusLancamentoEnum.CANCELADO || lancamento.getStatus() == StatusLancamentoEnum.AGUARDANDO_PAGAMENTO) && lancamento.getVenda().getTipo().equals("servico")) {
            Agendamento agendamento = lancamento.getVenda()
                    .getItensVenda()
                    .stream()
                    .findFirst()
                    .map(itemVenda -> itemVenda.getAgendamento()).orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado"));


            agendamentoService.desmarcar(agendamento);
        }
        validar(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public Lancamento atualizarEDesmarcar(Lancamento lancamento, Long idAgendamento) {
        Objects.requireNonNull(lancamento.getIdLancamento());
        if ((lancamento.getStatus() == StatusLancamentoEnum.CANCELADO || lancamento.getStatus() == StatusLancamentoEnum.AGUARDANDO_PAGAMENTO) && lancamento.getVenda().getTipo().equals("servico")) {
            Agendamento agendamento = agendamentoService.obterPorId(idAgendamento).orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado!"));

            agendamentoService.desmarcar(agendamento);

        }
        validar(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getIdLancamento());
        lancamentoRepository.delete(lancamento);
    }

    public Optional<Lancamento> obterPorId(Long id) {
        return lancamentoRepository.findById(id);
    }

    public List<Lancamento> listarTodos() {
        return lancamentoRepository.findAll();
    }

    public List<LancamentoDAO> listarReceitasOrdenados() {
        return lancamentoRepository.findLancamentosReceitaOrderedByDatUpdate();
    }

    public List<LancamentoDAO> listarDespesasOrdenados() {
        return lancamentoRepository.findLancamentosDespesaOrderedByDatUpdate();
    }

    public List<LancamentoDAO> findReceita(String busca) {
        return lancamentoRepository.findLancamentoReceita(busca);
    }

    public List<Lancamento> findByIdIn(LancamentoIdsDTO idsLancamento) {
        return lancamentoRepository.findByIdLancamentoIn(idsLancamento.getIdsLancamento());
    }

    public BigDecimal obterSaldo() {

        BigDecimal receitas = lancamentoRepository.obterSaldoPorTipoLancamentoStatus(TipoLancamentoEnum.RECEITA, StatusLancamentoEnum.CONCLUIDO);
        BigDecimal depositos = lancamentoRepository.obterSaldoPorTipoLancamentoStatus(TipoLancamentoEnum.DEPOSITO, StatusLancamentoEnum.CONCLUIDO);
        BigDecimal despesas = lancamentoRepository.obterSaldoPorTipoLancamentoStatus(TipoLancamentoEnum.DESPESA, StatusLancamentoEnum.CONCLUIDO);
        BigDecimal sangrias = lancamentoRepository.obterSaldoPorTipoLancamentoStatus(TipoLancamentoEnum.SANGRIA, StatusLancamentoEnum.CONCLUIDO);

        if (receitas == null) {
            receitas = BigDecimal.ZERO;
        }

        if (despesas == null) {
            despesas = BigDecimal.ZERO;
        }

        if (depositos == null) {
            depositos = BigDecimal.ZERO;
        }

        if (sangrias == null) {
            sangrias = BigDecimal.ZERO;
        }

        return receitas.add(depositos).subtract(despesas).subtract(sangrias);
    }


    public void validar(Lancamento lancamento) {

        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }

        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um usuário.");
        }

        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido.");
        }

        if (lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de lançamento.");
        }

    }

    public FechamentoCaixaResponseDTO fechamentoCaixa(List<Lancamento> lancamentos) {

        FechamentoCaixaResponseDTO response = new FechamentoCaixaResponseDTO();
        lancamentos.forEach(lancamento -> {

            if (lancamento.getTipo() == TipoLancamentoEnum.RECEITA) {

                if (lancamento.getVenda() != null) {
                    if (lancamento.getVenda().getTipo() != null) {
                        if (lancamento.getVenda().getTipo().equals("servico")) {
                            response.setServicos(response.servicos.add(lancamento.getValor()));
                        }
                        if (lancamento.getVenda().getTipo().equals("produto")) {
                            response.setProdutos(response.produtos.add(lancamento.getValor()));
                        }
                    }


                } else {
                    response.setOutrasReceitas(response.outrasReceitas.add(lancamento.getValor()));
                }

            } else if (lancamento.getTipo() == TipoLancamentoEnum.DEPOSITO) {
                response.setDepositos(response.depositos.add(lancamento.getValor()));
            } else if (lancamento.getTipo() == TipoLancamentoEnum.DESPESA) {
                response.setDespesas(response.despesas.add(lancamento.getValor()));
            } else if (lancamento.getTipo() == TipoLancamentoEnum.SANGRIA) {
                response.setSangrias(response.sangrias.add(lancamento.getValor()));
            }
        });

        response.setSubtotalEntrada(response.produtos.add(response.servicos.add(response.outrasReceitas.add(response.depositos))));
        response.setSubtotalSaida(response.sangrias.add(response.despesas));
        response.setTotalVendas(response.produtos.add(response.servicos));
        return response;


    }
}
