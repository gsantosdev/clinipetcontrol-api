package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.model.entity.EstabConfig;
import br.com.clinipet.ClinipetControl.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class EstabConfigController {

    private final ConfigService configService;

    @PutMapping
    public ResponseEntity alterarConfig(@RequestBody EstabConfig estabConfig) {
        try {
            return ResponseEntity.ok(configService.alterar(estabConfig));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getConfig() {
        try {
            return ResponseEntity.ok(configService.get());
        } catch (Exception e) {
            return ResponseEntity.ok(configService.alterar(EstabConfig.builder().horarioInicio("08:00").horarioFim("20:00").build()));
        }
    }


}
