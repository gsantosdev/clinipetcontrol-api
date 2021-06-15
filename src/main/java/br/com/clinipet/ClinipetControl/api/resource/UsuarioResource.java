package br.com.clinipet.ClinipetControl.api.resource;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.service.UsuarioService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = usuarioService.cadastrar(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
        return usuarioService.obterPorId(id).map(entity -> {
            try {
                usuario.setId(entity.getId());
                usuarioService.atualizar(usuario);
                return ResponseEntity.ok(usuario);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Usuário não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity obterPorId(@PathVariable("id") Long id){
        Optional<Usuario> usuario = usuarioService.obterPorId(id);

        if(usuario.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{nome}")
    public ResponseEntity obterPorNome(@PathVariable("id") String nome){
        Optional<Usuario> usuario = usuarioService.obterPorNome(nome);

        if(usuario.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario);
    }
}
