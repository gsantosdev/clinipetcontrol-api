package br.com.clinipet.ClinipetControl.service;


import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;


    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Optional<Cliente> obterPorId(Long id) {
        return repository.findById(id);
    }

}
