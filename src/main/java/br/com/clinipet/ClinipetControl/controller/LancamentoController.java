package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.controller.dto.request.LancamentoIdsDTO;
import br.com.clinipet.ClinipetControl.controller.dto.request.LancamentoRequestDTO;
import br.com.clinipet.ClinipetControl.controller.dto.request.StatusRequestDTO;
import br.com.clinipet.ClinipetControl.controller.dto.response.FechamentoCaixaResponseDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.LancamentoMapper;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.RegistroCaixa;
import br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO;
import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import br.com.clinipet.ClinipetControl.service.LancamentoService;
import br.com.clinipet.ClinipetControl.service.RegistroCaixaService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    private final LancamentoMapper lancamentoMapper;

    private final RegistroCaixaService registroCaixaService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoRequestDTO dto) {
        try {
            Lancamento lancamento = lancamentoMapper.toEntity(dto);
            lancamento = lancamentoService.salvar(lancamento);
            return new ResponseEntity(lancamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody StatusRequestDTO statusRequestDTO) {
        return lancamentoService.obterPorId(id).map(entity -> {
            StatusLancamentoEnum statusSelecionado = StatusLancamentoEnum.valueOf(statusRequestDTO.getStatus());
            if (statusSelecionado == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido!");
            }
            try {
                entity.setStatus(statusSelecionado);
                lancamentoService.atualizar(entity);
                return ResponseEntity.ok(entity);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() ->
                new ResponseEntity("Lancamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/atualiza-status-desmarca")
    public ResponseEntity atualizarStatusDesmarcando(@PathVariable("id") Long id, @RequestBody StatusRequestDTO statusRequestDTO) {
        return lancamentoService.obterPorId(id).map(entity -> {
            StatusLancamentoEnum statusSelecionado = StatusLancamentoEnum.valueOf(statusRequestDTO.getStatus());
            if (statusSelecionado == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido!");
            }
            try {
                entity.setStatus(statusSelecionado);
                lancamentoService.atualizarEDesmarcar(entity, entity.getIdAgendamento());
                return ResponseEntity.ok(entity);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() ->
                new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }


    @GetMapping
    public ResponseEntity listar() {

        List<Lancamento> lancamentos = lancamentoService.listarTodos();

        return ResponseEntity.ok(lancamentos);

    }

    @GetMapping("/listarReceitas")
    public ResponseEntity listarReceitas() {

        List<LancamentoDAO> lancamentos = lancamentoService.listarReceitasOrdenados();
        if (lancamentos.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(lancamentos);
    }


    @GetMapping("/listarDespesas")
    public ResponseEntity listarDespesas() {

        List<LancamentoDAO> lancamentos = lancamentoService.listarDespesasOrdenados();
        if (lancamentos.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/caixa")
    public ResponseEntity obterCaixa() {

        BigDecimal caixa = lancamentoService.obterSaldo();

        return ResponseEntity.ok(caixa);
    }

    @PostMapping("/findByIdIn")
    public ResponseEntity obterTodosPorIdEm(@RequestBody LancamentoIdsDTO idsLancamento) {

        try {
            List<Lancamento> lancamentos = lancamentoService.findByIdIn(idsLancamento);
            RegistroCaixa registroCaixa = RegistroCaixa.builder().lancamentos(lancamentos).fim(Date.from(Instant.now())).inicio(idsLancamento.getDataInicio()).build();
            registroCaixaService.salvar(registroCaixa);
            //lancamentoService.fechamentoCaixa(lancamentos);

            return ResponseEntity.ok(lancamentos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping("/relatorioFechamento")
    public ResponseEntity relatorioFechamento(@RequestBody LancamentoIdsDTO idsLancamento) {

        try {
            List<Lancamento> lancamentos = lancamentoService.findByIdIn(idsLancamento);
            RegistroCaixa registroCaixa = RegistroCaixa.builder().lancamentos(lancamentos).fim(Date.from(Instant.now())).inicio(idsLancamento.getDataInicio()).build();
            registroCaixaService.salvar(registroCaixa);
            FechamentoCaixaResponseDTO fechamentoCaixaResponseDTO = lancamentoService.fechamentoCaixa(lancamentos);

            fechamentoCaixaResponseDTO.setFim(DateUtils.addHours((Date.from(Instant.now())), -3));
            fechamentoCaixaResponseDTO.setInicio(DateUtils.addHours(idsLancamento.getDataInicio(), -3));

            return ResponseEntity.ok(fechamentoCaixaResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @GetMapping("/findReceita")
    public ResponseEntity findReceita(@RequestParam String busca) {
        List<LancamentoDAO> lancamentos = lancamentoService.findReceita(busca);
        if (lancamentos.isEmpty()) {
            return new ResponseEntity(lancamentos, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(lancamentos, HttpStatus.OK);
    }
}
