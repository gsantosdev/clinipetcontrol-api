package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.dao.ServicoDAO;
import br.com.clinipet.ClinipetControl.model.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository repository;

    private final ServicoService servicoService;

    @Transactional
    public Animal cadastrarAnimal(Animal animal) {
        validar(animal);
        animal.setDataCadastro(Date.from(Instant.now()));

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


    public List<ServicoDAO> obterHistorico(Long id) {
        return servicoService.getHistoricoAnimal(id);
    }

    public void validar(Animal animal) {

        if (animal.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome v??lido.");
        }
        if (animal.getSexo().trim().equals("")) {
            throw new RegraNegocioException("Informe um sexo v??lido.");
        }
        if (animal.getDataNascimento() == null) {
            throw new RegraNegocioException("Informe uma data de nascimento v??lida.");
        }
        if (animal.getDataNascimento().compareTo(new Date()) > 0) {
            throw new RegraNegocioException("Informe uma data de nascimento v??lida.");
        }
        if (animal.getRaca().trim().equals("")) {
            throw new RegraNegocioException("Informe uma ra??a v??lida.");
        }
        if (animal.getEspecie().trim().equals("")) {
            throw new RegraNegocioException("Informe uma esp??cie v??lida.");
        }
        if (animal.getPorte().trim().equals("")) {
            throw new RegraNegocioException("Informe um porte v??lido.");
        }
        if (animal.getCor().trim().equals("")) {
            throw new RegraNegocioException("Informe uma cor v??lida");
        }
        if (animal.getCliente() == null) {
            throw new RegraNegocioException("Informe um cliente v??lido.");
        }

    }
}
