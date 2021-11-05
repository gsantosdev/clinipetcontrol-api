package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.controller.dto.request.LancamentoRequestDTO;
import br.com.clinipet.ClinipetControl.controller.dto.request.StatusRequestDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.LancamentoMapper;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO;
import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import br.com.clinipet.ClinipetControl.service.LancamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    private final LancamentoMapper lancamentoMapper;

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
                new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }


    @GetMapping
    public ResponseEntity listar() {

        List<Lancamento> lancamentos = lancamentoService.listarTodos();

        return ResponseEntity.ok(lancamentos);

    }

    @GetMapping("/listarUpdated")
    public ResponseEntity listarPorDatUpdate() {

        List<LancamentoDAO> lancamentos = lancamentoService.listarOrdenados();
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
}
