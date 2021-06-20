package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        repository.delete(animal);
    }

    @Transactional
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

        if ( animal.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }

        //TODO Aumentar validações

    }
}
