package br.com.clinipet.ClinipetControl.controller;

import br.com.clinipet.ClinipetControl.controller.dto.request.UsuarioRequestDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.UsuarioMapper;
import br.com.clinipet.ClinipetControl.exception.ErroAutenticacao;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {

        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(usuarioRequestDTO.getNome(), usuarioRequestDTO.getSenha());
            return ResponseEntity.ok().body(usuarioAutenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {

        Usuario usuarioACadastrar = usuarioMapper.toEntity(usuarioRequestDTO);

        try {
            Usuario usuarioSalvo = usuarioService.cadastrar(usuarioACadastrar);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {

        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);

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
    public ResponseEntity obterPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.obterPorId(id);

        if (usuario.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity obterPorNomeTodos(@RequestParam("busca") String busca) {

        if (busca == null || busca.equals(Strings.EMPTY)) {
            return ResponseEntity.badRequest().body("A busca não pode estar vazia");
        }

        List<Usuario> usuario = usuarioService.obterTodosPorNome(busca);

        if (usuario.isEmpty()) {
            return new ResponseEntity(usuario, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return usuarioService.obterPorId(id).map(entity -> {
            usuarioService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Usuário não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/listar")
    public ResponseEntity listarUsuariosTodos(Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listarUsuarios(pageable);
        if (usuarios.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuarios);

    }


    @GetMapping("/listarPage")
    public ResponseEntity<Page<Usuario>> listarUsuarios(@RequestParam(value = "pg", required = false) Integer pg, @RequestParam(value = "qtd", required = false) Integer qtd) {

        Pageable pageable = PageRequest.of(pg, qtd);

        Page<Usuario> usuarios = usuarioService.listarUsuarios(pageable);
        if (usuarios.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuarios);

    }

    @GetMapping("/tipos")
    public ResponseEntity listarTiposDeUsuario() {
        List<Map<String, String>> tipos = usuarioService.listarNomesTipos();
        if (tipos.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tipos);

    }

}
