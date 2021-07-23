package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.controller.dto.request.AgendamentoRequestDTO;
import br.com.clinipet.ClinipetControl.controller.dto.response.AgendamentoResponseDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.service.AgendamentoService;
import br.com.clinipet.ClinipetControl.service.AnimalService;
import br.com.clinipet.ClinipetControl.service.FuncionarioService;
import br.com.clinipet.ClinipetControl.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    private final ServicoService servicoService;

    private final AnimalService animalService;

    private final FuncionarioService funcionarioService;


    @PostMapping
    public ResponseEntity marcar(@RequestBody AgendamentoRequestDTO dto) {

        try {
            Agendamento agendamento = converterParaAgendamento(dto);
            agendamentoService.marcar(agendamento);
            return new ResponseEntity(agendamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity remarcar(@PathVariable("id") Long id, @RequestBody AgendamentoRequestDTO dto) {
        Agendamento agendamentoARemarcar = converterParaAgendamento(dto);


        return agendamentoService.obterPorId(id).map(entity -> {
            try {
                agendamentoARemarcar.setId(entity.getId());
                agendamentoService.remarcar(agendamentoARemarcar);
                return ResponseEntity.ok(agendamentoARemarcar);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Agendamento não encontrado!", HttpStatus.BAD_REQUEST));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity desmarcar(@PathVariable("id") Long id) {
        return agendamentoService.obterPorId(id).map(entity -> {
            agendamentoService.desmarcar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Agendamento não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/listar")
    public ResponseEntity listar() {
        List<Agendamento> agendamentos = agendamentoService.obterTodos();

        List<AgendamentoResponseDTO> listagem = new ArrayList<>();

        agendamentos.forEach((agendamento -> {

            listagem.add(AgendamentoResponseDTO.builder().id(agendamento.getId())
                    .title(agendamento.getServico().getNome())
                    .start(agendamento.getDataInicio())
                    .end(agendamento.getDataFim())
                    .build());

        }));
        if (agendamentos.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listagem);
    }


    private Agendamento converterParaAgendamento(final AgendamentoRequestDTO agendamentoRequestDTO) {
        Agendamento agendamento = new Agendamento();

        agendamento.setDataInicio(agendamentoRequestDTO.getDataHorario());
        agendamento.setObservacoes(agendamentoRequestDTO.getObservacoes());
        agendamento.setDataFim(Date.from(agendamentoRequestDTO.getDataHorario().toInstant()
                .plusSeconds(agendamentoRequestDTO.getDuracaoAprox() * 60)));

        Servico servico = servicoService.obterPorId(agendamentoRequestDTO.getIdServico()).orElseThrow(() -> new RegraNegocioException("Serviço não encontrado!"));

        Animal animal = animalService.obterPorId(agendamentoRequestDTO.getIdAnimal()).orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));

        Funcionario funcionario = funcionarioService.obterPorId(agendamentoRequestDTO.getIdFuncionario()).orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado"));

        agendamento.setServico(servico);
        agendamento.setAnimal(animal);
        agendamento.setFuncionario(funcionario);

        return agendamento;
    }

}
