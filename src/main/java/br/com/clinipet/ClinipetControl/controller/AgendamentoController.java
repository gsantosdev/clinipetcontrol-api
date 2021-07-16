package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.controller.dto.AgendamentoDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    private final ServicoService servicoService;

    private final AnimalService animalService;

    private final FuncionarioService funcionarioService;


    @PostMapping
    public ResponseEntity marcar(@RequestBody AgendamentoDTO dto) {

        try {
            Agendamento agendamento = converterParaAgendamento(dto);
            agendamentoService.marcar(agendamento);
            return new ResponseEntity(agendamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PutMapping("/{id}")
    public ResponseEntity remarcar(@PathVariable("id") Long id, @RequestBody AgendamentoDTO dto) {
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


    private Agendamento converterParaAgendamento(final AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = new Agendamento();

        agendamento.setDataHorario(agendamentoDTO.getDataHorario());
        agendamento.setObservacoes(agendamentoDTO.getObservacoes());
        agendamento.setDuracaoAprox(agendamentoDTO.getDuracaoAprox());

        Servico servico = servicoService.obterPorId(agendamentoDTO.getIdServico()).orElseThrow(() -> new RegraNegocioException("Serviço não encontrado!"));

        Animal animal = animalService.obterPorId(agendamentoDTO.getIdAnimal()).orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));

        Funcionario funcionario = funcionarioService.obterPorId(agendamentoDTO.getIdFuncionario()).orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado"));

        agendamento.setServico(servico);
        agendamento.setAnimal(animal);
        agendamento.setFuncionario(funcionario);

        return agendamento;
    }

}
