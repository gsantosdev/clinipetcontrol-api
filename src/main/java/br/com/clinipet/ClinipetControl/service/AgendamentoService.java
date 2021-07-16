package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private void validar(Agendamento agendamento) {


    }


    public Optional<Agendamento> obterPorId(Long id) {
        return agendamentoRepository.findById(id);
    }
}
