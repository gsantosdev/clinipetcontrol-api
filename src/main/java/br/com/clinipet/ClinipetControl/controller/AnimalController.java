package br.com.clinipet.ClinipetControl.controller;


import br.com.clinipet.ClinipetControl.controller.dto.AnimalDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.service.AnimalService;
import br.com.clinipet.ClinipetControl.service.ClienteService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animais")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    private final ClienteService clienteService;


    @PostMapping
    public ResponseEntity cadastrar(@RequestBody AnimalDTO animalDTO) {
        try {
            Animal animalSalvo = converterParaAnimal(animalDTO);
            animalService.cadastrarAnimal(animalSalvo);
            return new ResponseEntity(animalSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AnimalDTO animalDTO) {

        Animal animalAAtualizar = converterParaAnimal(animalDTO);

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

    @GetMapping("/nome/{nome}")
    public ResponseEntity obterPorNome(@PathVariable("nome") String nome) {
        List<Animal> animal = animalService.obterPorNome(nome);

        if (animal.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animal);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return animalService.obterPorId(id).map(entity -> {
            animalService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Animal não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity listarAnimais() {
        List<Animal> animais = animalService.listarAnimais();
        if (animais.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animais);


    }

    private Animal converterParaAnimal(final AnimalDTO animalDTO) {
        Animal animal = new Animal();

        animal.setNome(animalDTO.getNome());
        animal.setSexo(animalDTO.getSexo());
        animal.setIdade(animalDTO.getIdade());
        animal.setRaca(animalDTO.getRaca());
        animal.setCor(animalDTO.getCor());
        animal.setTipo(animalDTO.getTipo());
        animal.setPeso(animalDTO.getPeso());
        animal.setAlergias(animalDTO.getAlergias());
        animal.setPatologias(animalDTO.getPatologias());
        animal.setMedicamentos(animalDTO.getMedicamentos());


        Cliente cliente = clienteService.obterPorId(animalDTO.getIdCliente()).orElseThrow(() -> new RegraNegocioException("Cliente não encontrado!"));

        animal.setCliente(cliente);

        return animal;
    }


}
