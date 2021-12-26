package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.ErroAutenticacao;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.model.enums.TipoUsuarioEnum;
import br.com.clinipet.ClinipetControl.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public Usuario autenticar(String nome, String senha) {
        Optional<Usuario> usuario = repository.findByNomeAndAtivoTrue(nome);

        if (usuario.isEmpty()) {
            throw new ErroAutenticacao("Usuario não encontrado com o nome informado!");
        }

        if (!matches(senha, usuario.get().getSenha())) {
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();
    }


    @Transactional
    public Usuario cadastrar(Usuario usuario) {
        validar(usuario);
        usuario.setSenha(encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Usuario usuario) {
        Objects.requireNonNull(usuario);
        validar(usuario);
        usuario.setSenha(encode(usuario.getSenha()));
        return repository.save(usuario);
    }


    @Transactional
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Usuario> obterTodosPorNome(String nome) {
        return repository.findAllByNomeAndAtivoTrue(nome);
    }


    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return repository.findAll(pageable);
    }


    public List<Map<String, String>> listarNomesTipos() {
        return TipoUsuarioEnum.listarNomesTipos();
    }

    public List<TipoUsuarioEnum> listarTiposDeUsuario() {
        return TipoUsuarioEnum.getTipos();
    }

    @Transactional
    public void deletar(Usuario usuario) {
        Objects.requireNonNull(usuario.getId());
        repository.delete(usuario);

    }

    public void validar(Usuario usuario) {

        if (usuario.getNome() == null || usuario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }
        if (usuario.getId() == null && !repository.findAllByNomeAndAtivoTrue(usuario.getNome()).isEmpty()) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este nome.");

        }
        if (!repository.findEqualNomesAndAtivoTrue(usuario.getNome(), usuario.getId()).isEmpty()) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este nome.");
        }
        if (usuario.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo válido.");
        }

    }

    public String encode(String senha) {
        StandardPasswordEncoder encoder = new StandardPasswordEncoder("secret");
        return encoder.encode(senha);
    }

    public boolean matches(String senha, String senhaArmazenada) {
        StandardPasswordEncoder encoder = new StandardPasswordEncoder("secret");
        return encoder.matches(senha, senhaArmazenada);
    }
}
