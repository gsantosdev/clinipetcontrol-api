package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.model.entity.dao.ContagemServicoDAO;
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

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Servico servico) {
        try {
            Servico servicoSalvo = servicoService.cadastrar(servico);
            return new ResponseEntity(servicoSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Servico servico) {

        //Acha a espécie com o ID informado, seta o mesmo id para o servico que veio via body, e salva essa mesma espécie.
        return servicoService.obterPorId(id).map(entity -> {
            try {
                servico.setId(entity.getId());
                servicoService.atualizar(servico);
                return ResponseEntity.ok(servico);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Servico não encontrada!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Servico> servico = servicoService.obterPorId(id);

        if (servico.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(servico);
    }

    @GetMapping("/{id}/valor")
    public ResponseEntity obterValorVendaPorId(@PathVariable("id") Long id) {
        try {
            BigDecimal servico = servicoService.obterValorVenda(id);

            if (servico == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(servico);
        } catch (RegraNegocioException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/listar")
    public ResponseEntity listarServicos() {
        List<Servico> servicos = servicoService.listarServicos();
        if (servicos.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(servicos);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        try {
            return servicoService.obterPorId(id).map(entity -> {
                servicoService.deletar(entity);
                return new ResponseEntity(HttpStatus.NO_CONTENT);

            }).orElseGet(() -> new ResponseEntity("Serviço não encontrado!", HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new ResponseEntity("O serviço ainda possui um agendamento!", HttpStatus.FORBIDDEN);
            }
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/listarNomes")
    public ResponseEntity listarServicosLabelValue() {
        List<Map<String, Object>> listaNomes = servicoService.listarNomes();
        if (listaNomes.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaNomes);


    }

    @GetMapping("/contagemServicos")
    public ResponseEntity contagemServicos() {
        List<ContagemServicoDAO> contagemServicoDAOS = servicoService.contagemServicoDAOS();

        if (contagemServicoDAOS.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contagemServicoDAOS);
    }

}
