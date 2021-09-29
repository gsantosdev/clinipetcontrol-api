package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.ErroAutenticacao;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.model.enums.TipoUsuarioEnum;
import br.com.clinipet.ClinipetControl.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
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
        validar(usuario);
        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Usuario usuario) {
        Objects.requireNonNull(usuario);
        validar(usuario);
        return repository.save(usuario);
    }


    @Transactional
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }


    public List<Usuario> obterPorNome(String nome) {
        return repository.findAllByNome(nome).get();
    }

    @Transactional
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
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

        if (!repository.findEqualNomes(usuario.getNome(), usuario.getId()).isEmpty()) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este nome.");
        }

        if (usuario.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo válido.");
        }

    }
}
