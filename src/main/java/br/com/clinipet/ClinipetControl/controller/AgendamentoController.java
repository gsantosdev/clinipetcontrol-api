package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.controller.dto.request.AgendamentoRequestDTO;
import br.com.clinipet.ClinipetControl.controller.dto.response.AgendamentoResponseDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.AgendamentoMapper;
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

    private final AgendamentoMapper agendamentoMapper;


    @PostMapping
    public ResponseEntity marcar(@RequestBody AgendamentoRequestDTO dto) {

        try {
            Agendamento agendamento = agendamentoMapper.toEntity(dto);
            agendamentoService.marcar(agendamento);
            return new ResponseEntity(agendamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity remarcar(@PathVariable("id") Long id, @RequestBody AgendamentoRequestDTO dto) {
        Agendamento agendamentoARemarcar = agendamentoMapper.toEntity(dto);


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
                    .title(agendamento.getServico().getNome()+" - "+agendamento.getAnimal().getNome())
                    .start(agendamento.getDataInicio())
                    .end(agendamento.getDataFim())
                    .nomeFuncionario(agendamento.getFuncionario().getNome())
                    .nomeAnimal(agendamento.getAnimal().getNome())
                    .nomeProprietario(agendamento.getAnimal().getCliente().getNome())
                    .nomeServico(agendamento.getServico().getNome())
                    .telefoneProprietario(agendamento.getAnimal().getCliente().getTelefone())
                    .build());

        }));

        if (agendamentos.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listagem);
    }

}
