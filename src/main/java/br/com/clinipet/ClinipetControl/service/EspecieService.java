package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.Especie;
import br.com.clinipet.ClinipetControl.model.repository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspecieService {

    private final EspecieRepository especieRepository;

    @Transactional
    public Especie cadastrar(Especie especie) {
        validar(especie);
        return especieRepository.save(especie);
    }

    @Transactional
    public Especie atualizar(Especie especie) {
        Objects.requireNonNull(especie.getId());
        validar(especie);
        return especieRepository.save(especie);
    }

    public List<Especie> listarEspecies() {
        return especieRepository.findAll();
    }

    @Transactional
    public void deletar(Especie especie) {
        Objects.requireNonNull(especie.getId());
        especieRepository.delete(especie);
    }




    public Optional<Especie> obterPorId(Long id) {
        return especieRepository.findById(id);
    }

    public void validar(Especie especie) {

        if (especie.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }

        //TODO Aumentar validações

    }

}
