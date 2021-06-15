package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.ErroAutenticacao;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public Usuario autenticar(String nome, String senha) {
        Optional<Usuario> usuario = repository.findByNome(nome);

        if (usuario.isEmpty()) {
            throw new ErroAutenticacao("Usuario não encontrado com o nome informado!");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();
    }


    @Transactional
    public Usuario cadastrar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Usuario usuario) {
        Objects.requireNonNull(usuario);
        return repository.save(usuario);
    }

    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Usuario> obterPorNome(String nome) {
        return repository.findByNome(nome);
    }


    @Transactional
    public void deletar(Usuario usuario) {
        Objects.requireNonNull(usuario.getId());
        repository.delete(usuario);

    }
}
