package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.Especie;
import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import br.com.clinipet.ClinipetControl.model.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;


    @Transactional
    public Funcionario cadastrar(Funcionario funcionario) {
        //validar(animal);
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public Funcionario atualizar(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        //validar(especie);
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarFuncionarios() { return funcionarioRepository.findAll(); }

    public Optional<List<Funcionario>> obterFuncionarioPorNomeTelefone(String busca){
        return funcionarioRepository.findByNomeOrTelefone(busca);
    }

    @Transactional
    public void deletar(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        funcionarioRepository.delete(funcionario);
    }

    public Optional<Funcionario> obterPorId(Long id) { return funcionarioRepository.findById(id); }

}
