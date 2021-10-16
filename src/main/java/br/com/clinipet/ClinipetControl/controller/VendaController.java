package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.controller.dto.request.VendaDTO;
import br.com.clinipet.ClinipetControl.exception.AgendamentoException;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Venda;
import br.com.clinipet.ClinipetControl.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody VendaDTO vendaDTO) {
        try {
            Venda vendaSalva = vendaService.efetuarVenda(vendaDTO);
            return new ResponseEntity(vendaSalva, HttpStatus.ACCEPTED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AgendamentoException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        try {
            Venda venda = vendaService.obterVenda(id);
            return new ResponseEntity(venda, HttpStatus.OK);
        } catch (RegraNegocioException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity listarVendas() {

        List<Venda> vendas = vendaService.listarTodas();
        if (vendas.isEmpty()) {
            return new ResponseEntity(vendas, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(vendas, HttpStatus.OK);

    }

    @GetMapping("/listar/{id}")
    public ResponseEntity listarVendasPorIdCliente(@PathVariable("id") Long idCliente) {

        List<Venda> vendas = vendaService.listarTodasPorIdCliente(idCliente);
        if (vendas.isEmpty()) {
            return new ResponseEntity(vendas, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(vendas, HttpStatus.OK);

    }

}
