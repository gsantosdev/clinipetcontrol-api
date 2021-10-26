package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Produto;
import br.com.clinipet.ClinipetControl.model.enums.StatusEstoqueEnum;
import br.com.clinipet.ClinipetControl.service.ProdutoService;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Produto produto) {
        try {
            Produto produtoSalvo = produtoService.cadastrar(produto);
            return new ResponseEntity(produtoSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Produto produto) {

        //Acha o registro com o ID informado, seta o mesmo id para o registro que veio via body, e sobrescreve esse mesmo registro.
        return produtoService.obterPorId(id).map(entity -> {
            try {
                produto.setId(entity.getId());
                produtoService.atualizar(produto);
                return ResponseEntity.ok(produto);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Servico não encontrada!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoService.obterPorId(id);

        if (produto.isEmpty()) {
            return new ResponseEntity("Produto não encontrado!", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(produto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        try {
            return produtoService.obterPorId(id).map(entity -> {
                produtoService.deletar(entity);
                return new ResponseEntity(HttpStatus.NO_CONTENT);

            }).orElseGet(() -> new ResponseEntity("Produto não encontrado!", HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new ResponseEntity("O produto possui uma venda!", HttpStatus.FORBIDDEN);
            }
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/{id}/baixaEstoque")
    public ResponseEntity baixaEstoque(@PathVariable("id") Long id) {
        try {
            Produto produto = produtoService.baixaEstoque(id);
            return new ResponseEntity(produto, HttpStatus.OK);
        } catch (RegraNegocioException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/entradaEstoque")
    public ResponseEntity entradaEstoque(@PathVariable("id") Long id) {
        try {
            Produto produto = produtoService.entradaEstoque(id);
            return new ResponseEntity(produto, HttpStatus.OK);
        } catch (RegraNegocioException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/statusEstoque")
    public ResponseEntity statusEstoque(@PathVariable("id") Long id) {
        try {
            StatusEstoqueEnum produto = produtoService.getStatusEstoque(id);
            return new ResponseEntity(produto.name(), HttpStatus.OK);
        } catch (RegraNegocioException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
