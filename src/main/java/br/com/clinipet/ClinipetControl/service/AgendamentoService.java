package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.AgendamentoException;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.repository.AgendamentoRepository;
import br.com.clinipet.ClinipetControl.model.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    private final LancamentoRepository lancamentoRepository;

    private final ConfigService configService;

    @Transactional
    public Agendamento marcar(Agendamento agendamento) {
        validar(agendamento);
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento remarcar(Agendamento agendamento, Long idLancamento) {
        Objects.requireNonNull(agendamento.getId());
        validarRemarcar(agendamento);
        Agendamento save = agendamentoRepository.save(agendamento);

        Lancamento lancamento = lancamentoRepository.findById(idLancamento).orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
        lancamento.setDescricao(agendamento.getServico().getNome() + " - " + agendamento.getAnimal().getNome());
        lancamento.setDataExecucao(agendamento.getDataInicio());
        lancamentoRepository.save(lancamento);

        return save;

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


    public void validar(Agendamento agendamento) {

        List<Agendamento> agendamentos = agendamentoRepository.findExistentAgendamentosByRange(agendamento.getDataInicio(),
                agendamento.getDataFim(), agendamento.getFuncionario().getId(), agendamento.getAnimal().getId());

        Date dataInicio = DateUtils.addHours(agendamento.getDataInicio(), 3);

        Date dataFim = DateUtils.addHours(agendamento.getDataFim(), 3);

        var horaInicioAgendamento = dataInicio.getHours();
        var minutoInicioAgendamento = dataInicio.getMinutes();

        var horaFimAgendamento = dataFim.getHours();
        var minutoFimAgendamento = dataFim.getMinutes();


        int horaConfigInicio = Integer.parseInt(configService.get().getHorarioInicio().split(":")[0]);
        int minutosConfigInicio = Integer.parseInt(configService.get().getHorarioInicio().split(":")[1]);

        int horaConfigFim = Integer.parseInt(configService.get().getHorarioFim().split(":")[0]);
        int minutosConfigFim = Integer.parseInt(configService.get().getHorarioFim().split(":")[1]);

        if (!compareTime(horaConfigInicio, minutosConfigInicio, horaConfigFim, minutosConfigFim, horaInicioAgendamento, minutoInicioAgendamento, horaFimAgendamento, minutoFimAgendamento)) {
            throw new AgendamentoException("Agendamento fora do horário de funcionamento.");

        } else if (!agendamentos.isEmpty()) {
            throw new AgendamentoException("O funcionário(a) ou o animal já possui um agendamento no mesmo horário.");
        }
        if (dataInicio.compareTo(new Date(System.currentTimeMillis())) < 0) {
            throw new RegraNegocioException("Selecione um data e hora válida.");
        }
        if (agendamento.getFuncionario() == null) {
            throw new RegraNegocioException("Selecione um funcionário.");
        }
        if (agendamento.getAnimal() == null) {
            throw new RegraNegocioException("Selecione um animal.");
        }
        if (agendamento.getServico() == null) {
            throw new RegraNegocioException("Selecione um serviço.");
        }
    }

    public void validarRemarcar(Agendamento agendamento) {

        List<Agendamento> agendamentos = agendamentoRepository.findExistentAgendamentosByRangeRemarcar(agendamento.getDataInicio(),
                agendamento.getDataFim(), agendamento.getFuncionario().getId(), agendamento.getAnimal().getId(), agendamento.getId());

        Date dataInicio = DateUtils.addHours(agendamento.getDataInicio(), 3);

        Date dataFim = DateUtils.addHours(agendamento.getDataFim(), 3);

        var horaInicioAgendamento = dataInicio.getHours();
        var minutoInicioAgendamento = dataInicio.getMinutes();

        var horaFimAgendamento = dataFim.getHours();
        var minutoFimAgendamento = dataFim.getMinutes();


        int horaConfigInicio = Integer.parseInt(configService.get().getHorarioInicio().split(":")[0]);
        int minutosConfigInicio = Integer.parseInt(configService.get().getHorarioInicio().split(":")[1]);

        int horaConfigFim = Integer.parseInt(configService.get().getHorarioFim().split(":")[0]);
        int minutosConfigFim = Integer.parseInt(configService.get().getHorarioFim().split(":")[1]);


        if (!compareTime(horaConfigInicio, minutosConfigInicio, horaConfigFim, minutosConfigFim, horaInicioAgendamento, minutoInicioAgendamento, horaFimAgendamento, minutoFimAgendamento)) {
            throw new AgendamentoException("Agendamento fora do horário de funcionamento.");

        } else if (!agendamentos.isEmpty()) {
            throw new AgendamentoException("O funcionário(a) ou o animal já possui um agendamento no mesmo horário.");
        }
        if (dataInicio.compareTo(new Date(System.currentTimeMillis())) < 0) {
            throw new RegraNegocioException("Selecione um data e hora válida.");
        }
        if (agendamento.getFuncionario() == null) {
            throw new RegraNegocioException("Selecione um funcionário.");
        }
        if (agendamento.getAnimal() == null) {
            throw new RegraNegocioException("Selecione um animal.");
        }
        if (agendamento.getServico() == null) {
            throw new RegraNegocioException("Selecione um serviço.");
        }
    }

    public static boolean compareTime(int hei, int mei, int hef, int mef, int hia, int mia, int hfa, int mfa) {
        final LocalTime horarioEstabInicio = new LocalTime(hei, mei);
        final LocalTime horarioEstabFim = new LocalTime(hef, mef);

        final LocalTime horarioInicioAgendamento = new LocalTime(hia, mia);
        final LocalTime horarioFimAgendamento = new LocalTime(hfa, mfa);

        return !horarioInicioAgendamento.isBefore(horarioEstabInicio) && !horarioFimAgendamento.isAfter(horarioEstabFim) &&
                horarioInicioAgendamento.isBefore(horarioFimAgendamento) && horarioFimAgendamento.isAfter(horarioInicioAgendamento);

    }

}
