package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository repository;

    @Transactional
    public Animal cadastrarAnimal(Animal animal) {
        validar(animal);
        return repository.save(animal);
    }

    @Transactional
    public Animal atualizar(Animal animal) {
        Objects.requireNonNull(animal.getId());
        validar(animal);
        return repository.save(animal);
    }

    @Transactional
    public void deletar(Animal animal) {
        Objects.requireNonNull(animal.getId());
        try {
            repository.delete(animal);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("O animal ainda possui um agendamento!");
            }
            e.printStackTrace();
        }
    }

    public List<Animal> listarAnimais() {
        return repository.findAll();
    }


    public Optional<Animal> obterPorId(Long id) {
        return repository.findById(id);
    }


    public List<Animal> obterPorNome(String nome) {
        return repository.findByNome(nome).get();
    }

    public void validar(Animal animal) {

        if (animal.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }
        if (animal.getSexo().trim().equals("")) {
            throw new RegraNegocioException("Informe um sexo válido.");
        }
        if (animal.getDataNascimento() == null) {
            throw new RegraNegocioException("Informe uma data de nascimento válida.");
        }
        if (animal.getDataNascimento().compareTo(new Date()) > 0) {
            throw new RegraNegocioException("Informe uma data de nascimento válida.");
        }
        if (animal.getRaca().trim().equals("")) {
            throw new RegraNegocioException("Informe uma raça válida.");
        }
        if (animal.getEspecie().trim().equals("")) {
            throw new RegraNegocioException("Informe uma espécie válida.");
        }
        if (animal.getPorte().trim().equals("")) {
            throw new RegraNegocioException("Informe um porte válido.");
        }
        if (animal.getCor().trim().equals("")) {
            throw new RegraNegocioException("Informe uma cor válida");
        }
        if (animal.getCliente() == null) {
            throw new RegraNegocioException("Informe um cliente válido.");
        }


    }
}
