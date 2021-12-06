package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class EstabConfigController {

    private final ConfigService configService;

    @PutMapping
    public ResponseEntity alterarConfig() {

        return ResponseEntity.ok().build();
    }
}
