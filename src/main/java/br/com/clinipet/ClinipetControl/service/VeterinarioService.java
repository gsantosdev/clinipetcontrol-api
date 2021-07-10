package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Especie;
import br.com.clinipet.ClinipetControl.model.entity.Veterinario;
import br.com.clinipet.ClinipetControl.model.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;


    @Transactional
    public Veterinario cadastrar(Veterinario veterinario) {
        //validar(animal);
        return veterinarioRepository.save(veterinario);
    }

    @Transactional
    public Veterinario atualizar(Veterinario veterinario) {
        Objects.requireNonNull(veterinario.getId());
        //validar(especie);
        return veterinarioRepository.save(veterinario);
    }

    public List<Veterinario> listarVeterinarios() { return veterinarioRepository.findAll(); }

    @Transactional
    public void deletar(Veterinario veterinario) {
        Objects.requireNonNull(veterinario.getId());
        veterinarioRepository.delete(veterinario);
    }

    public Optional<Veterinario> obterPorId(Long id) { return veterinarioRepository.findById(id); }

}
