package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.controller.dto.UsuarioDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.service.AnimalService;
import br.com.clinipet.ClinipetControl.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = clienteService.cadastrar(cliente);
            return new ResponseEntity(clienteSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Cliente cliente) {

        return clienteService.obterPorId(id).map(entity -> {
            try {
                cliente.setId(entity.getId());
                clienteService.atualizar(cliente);
                return ResponseEntity.ok(cliente);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Cliente não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteService.obterPorId(id);

        if (cliente.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return clienteService.obterPorId(id).map(entity -> {
            clienteService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Cliente não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}/animais")
    public ResponseEntity listarAnimais(@PathVariable("id") Long id) {
        return clienteService.obterPorId(id).map(cliente -> {
            List<Animal> animais = clienteService.obterAnimais(cliente);
            if (animais.isEmpty()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(animais);

        }).orElseGet(() -> new ResponseEntity("Cliente não encontrado!", HttpStatus.BAD_REQUEST));

    }


}




