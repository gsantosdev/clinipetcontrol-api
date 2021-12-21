package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.EstabConfig;
import br.com.clinipet.ClinipetControl.model.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;

    public EstabConfig alterar(EstabConfig config) {

        int horaConfigInicio = Integer.parseInt(config.getHorarioInicio().split(":")[0]);
        int minutosConfigInicio = Integer.parseInt(config.getHorarioInicio().split(":")[1]);

        int horaConfigFim = Integer.parseInt(config.getHorarioFim().split(":")[0]);
        int minutosConfigFim = Integer.parseInt(config.getHorarioFim().split(":")[1]);

        if (compareTime(horaConfigInicio, minutosConfigInicio, horaConfigFim, minutosConfigFim)) {
            return configRepository.save(config);
        }

        throw new RegraNegocioException("Horário informado é inválido.");


    }

    public EstabConfig get() {
        return configRepository.findById(1L).orElseThrow(() -> new RegraNegocioException("Erro ao recuperar horario"));
    }

    public static boolean compareTime(int hei, int mei, int hef, int mef) {
        final LocalTime horarioEstabInicio = new LocalTime(hei, mei);
        final LocalTime horarioEstabFim = new LocalTime(hef, mef);

        return horarioEstabFim.isAfter(horarioEstabInicio);

    }

}
