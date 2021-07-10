package br.com.clinipet.ClinipetControl.model.entity;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.service.EspecieService;
import br.com.clinipet.ClinipetControl.service.VeterinarioService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Veterinario veterinario) {
        try {
            Veterinario veterinarioSalvo = veterinarioService.cadastrar(veterinario);
            return new ResponseEntity(veterinarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Veterinario veterinario) {

        return veterinarioService.obterPorId(id).map(entity -> {
            try {
                veterinario.setId(entity.getId());
                veterinarioService.atualizar(veterinario);
                return ResponseEntity.ok(veterinario);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Veterinário não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = veterinarioService.obterPorId(id);

        if (veterinario.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario);
    }

    @GetMapping("/listar")
    public ResponseEntity listar() {
        List<Veterinario> veterinario = veterinarioService.listarVeterinarios();
        if (veterinario.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return veterinarioService.obterPorId(id).map(entity -> {
            veterinarioService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Espécie não encontrada!", HttpStatus.BAD_REQUEST));
    }

}
