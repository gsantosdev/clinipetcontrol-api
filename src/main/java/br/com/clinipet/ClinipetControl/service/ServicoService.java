package br.com.clinipet.ClinipetControl.service;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.model.entity.dao.ServicoDAO;
import br.com.clinipet.ClinipetControl.model.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    @Transactional
    public Servico cadastrar(Servico servico) {
        validar(servico);
        return servicoRepository.save(servico);
    }

    @Transactional
    public Servico atualizar(Servico servico) {
        Objects.requireNonNull(servico.getId());
        validar(servico);
        return servicoRepository.save(servico);
    }

    public List<ServicoDAO> getHistoricoAnimal(Long id) {
        return servicoRepository.getHistorico(id);
    }

    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    @Transactional
    public void deletar(Servico servico) {
        Objects.requireNonNull(servico.getId());
        try {
            servicoRepository.delete(servico);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("Existe um agendamento com este serviço!");
            }
            e.printStackTrace();
        }
    }

    public Optional<Servico> obterPorId(Long id) {
        return servicoRepository.findById(id);
    }

    public BigDecimal obterValorVenda(Long id) {
        return obterPorId(id).map(Servico::getValorItem)
                .map(BigDecimal::valueOf)
                .orElseThrow(() -> new RegraNegocioException("Serviço não existente!"));
    }

    public void validar(Servico servico) {

        if (servico.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }
    }


    public List<Map<String, Object>> listarNomes() {

        List<Map<String, Object>> listaNomes = new ArrayList<>();
        servicoRepository.findAll().forEach(servico -> {
            Map<String, Object> servicos = new HashMap<>();
            servicos.put("label", servico.getNome());
            servicos.put("value", servico.getId());
            listaNomes.add(servicos);
        });

        return listaNomes;
    }
}
