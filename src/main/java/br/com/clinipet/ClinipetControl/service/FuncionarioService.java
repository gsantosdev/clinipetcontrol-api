package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import br.com.clinipet.ClinipetControl.model.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
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

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<List<Funcionario>> obterFuncionarioPorNomeTelefone(String busca) {
        return funcionarioRepository.findByNomeOrTelefone(busca);
    }

    @Transactional
    public void deletar(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        try {
            funcionarioRepository.delete(funcionario);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("O funcionário ainda possui um agendamento!");
            }
            e.printStackTrace();
        }
    }

    public Optional<Funcionario> obterPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

}
