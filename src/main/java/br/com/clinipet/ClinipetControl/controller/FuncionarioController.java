package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import br.com.clinipet.ClinipetControl.service.FuncionarioService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Funcionario funcionario) {
        try {
            Funcionario funcionarioSalvo = funcionarioService.cadastrar(funcionario);
            return new ResponseEntity(funcionarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Funcionario funcionario) {

        return funcionarioService.obterPorId(id).map(entity -> {
            try {
                funcionario.setId(entity.getId());
                funcionarioService.atualizar(funcionario);
                return ResponseEntity.ok(funcionario);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Veterinário não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioService.obterPorId(id);

        if (funcionario.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario);
    }

    @GetMapping("/listar")
    public ResponseEntity listar() {
        List<Funcionario> funcionario = funcionarioService.listarFuncionarios();
        if (funcionario.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario);

    }


    @GetMapping
    public ResponseEntity obterPorNomeTelefoneCpf(@RequestParam String busca) {
        Optional<List<Funcionario>> funcionario = funcionarioService.obterFuncionarioPorNomeTelefone(busca);

        if (funcionario.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return funcionarioService.obterPorId(id).map(entity -> {
            funcionarioService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Espécie não encontrada!", HttpStatus.BAD_REQUEST));
    }

}
