package br.com.clinipet.ClinipetControl.service;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.model.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
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

    public void validar(Servico servico) {

        if (servico.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }
    }
}
