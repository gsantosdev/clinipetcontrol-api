package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.controller.dto.request.AnimalRequestDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.AnimalMapper;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.service.AnimalService;
import br.com.clinipet.ClinipetControl.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animais")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    private final AnimalMapper animalMapper;


    @PostMapping
    public ResponseEntity cadastrar(@RequestBody AnimalRequestDTO animalRequestDTO) {
        try {
            Animal animalSalvo = animalMapper.toEntity(animalRequestDTO);
            animalService.cadastrarAnimal(animalSalvo);
            return new ResponseEntity(animalSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AnimalRequestDTO animalRequestDTO) {

        Animal animalAAtualizar = animalMapper.toEntity(animalRequestDTO);

        return animalService.obterPorId(id).map(entity -> {
            try {
                animalAAtualizar.setId(entity.getId());
                animalService.atualizar(animalAAtualizar);
                return ResponseEntity.ok(animalAAtualizar);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Animal não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Animal> animal = animalService.obterPorId(id);

        if (animal.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animal);
    }

    @GetMapping
    public ResponseEntity obterPorNome(@RequestParam("busca") String busca) {

        if(busca == null || busca.equals(Strings.EMPTY) ){
            return ResponseEntity.badRequest().body("A busca não pode estar vazia");
        }

        List<Animal> animal = animalService.obterPorNome(busca);

        if (animal.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animal);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {

        try {
            return animalService.obterPorId(id).map(entity -> {
                animalService.deletar(entity);
                return new ResponseEntity(HttpStatus.NO_CONTENT);

            }).orElseGet(() -> new ResponseEntity("Animal não encontrado!", HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new ResponseEntity("O animal ainda possui um agendamento!", HttpStatus.FORBIDDEN);
            }
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/listar")
    public ResponseEntity listarAnimais() {
        List<Animal> animais = animalService.listarAnimais();
        if (animais.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animais);
    }


}
