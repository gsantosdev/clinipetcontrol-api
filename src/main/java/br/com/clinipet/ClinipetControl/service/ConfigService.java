package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.EstabConfig;
import br.com.clinipet.ClinipetControl.model.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private ConfigRepository configRepository;

    public EstabConfig alterar(EstabConfig config) {

        return configRepository.save(config);

    }


}
