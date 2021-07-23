package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {


    private final AgendamentoRepository agendamentoRepository;

    @Transactional
    public Agendamento marcar(Agendamento agendamento) {
        validar(agendamento);
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento remarcar(Agendamento agendamento) {
        Objects.requireNonNull(agendamento.getId());
        validar(agendamento);
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void desmarcar(Agendamento agendamento) {
        Objects.requireNonNull(agendamento.getId());
        agendamentoRepository.delete(agendamento);
    }

    public Optional<Agendamento> obterPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    public List<Agendamento> obterTodos() {
        return agendamentoRepository.findAll();
    }

    private void validar(Agendamento agendamento) {

        List<Agendamento> agendamentos = agendamentoRepository.findExistentAgendamentosByRange(agendamento.getDataInicio(),
                agendamento.getDataFim(), agendamento.getFuncionario().getId(), agendamento.getAnimal().getId());

        if (!agendamentos.isEmpty()) {
            throw new RegraNegocioException("O funcionário(a) ou o animal já possui um agendamento no mesmo horário.");
        }

    }


}
