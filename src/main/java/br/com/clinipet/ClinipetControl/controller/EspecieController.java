package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Especie;
import br.com.clinipet.ClinipetControl.service.EspecieService;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/especies")
@RequiredArgsConstructor
public class EspecieController {

    private final EspecieService especieService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Especie especie) {
        try {
            Especie especieSalva = especieService.cadastrar(especie);
            return new ResponseEntity(especieSalva, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Especie especie) {

        //Acha a espécie com o ID informado, seta o mesmo id para a especie que veio via body, e salva essa mesma espécie.
        return especieService.obterPorId(id).map(entity -> {
            try {
                especie.setId(entity.getId());
                especieService.atualizar(especie);
                return ResponseEntity.ok(especie);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Especie não encontrada!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Especie> especie = especieService.obterPorId(id);

        if (especie.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especie);
    }

    @GetMapping("/listar")
    public ResponseEntity listarEspecies() {
        List<Especie> especies = especieService.listarEspecies();
        if (especies.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especies);
    }

    @GetMapping("/listarNomes")
    public ResponseEntity listarNomesEspecies() {
        List<Map<String, String>> nomes = especieService.listarNomesEspecies();
        if (nomes.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(nomes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return especieService.obterPorId(id).map(entity -> {
            especieService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Espécie não encontrada!", HttpStatus.BAD_REQUEST));
    }
}
