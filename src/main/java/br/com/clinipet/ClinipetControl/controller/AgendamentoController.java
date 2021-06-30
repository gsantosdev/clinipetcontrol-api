package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;


    @PostMapping
    public ResponseEntity marcar(@RequestBody Agendamento agendamento) {
        return null;
    }

    @PutMapping
    public ResponseEntity remarcar(@RequestBody Agendamento agendamento) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity desmarcar(@RequestBody Agendamento agendamento) {
        return null;
    }


}
