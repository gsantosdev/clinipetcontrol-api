package br.com.clinipet.ClinipetControl.service;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;


    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        validar(cliente);
        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("CPF já cadastrado");
            }
            e.printStackTrace();
            return null;
        }

    }


    public Optional<List<Cliente>> obterClientePorNomeCpfTelefone(String busca) {
        return clienteRepository.findByNomeOrTelefoneOrCpf(busca);
    }

    public Optional<Cliente> obterPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        validar(cliente);
        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("CPF já cadastrado");
            }
            e.printStackTrace();
            return null;

        }
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }


    public List<Animal> obterAnimais(Cliente cliente) {
        List<Animal> animais = cliente.getAnimais();
        if (animais.isEmpty()) {
            return Collections.emptyList();
        }

        return animais;
    }

    @Transactional
    public void deletar(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        try {
            clienteRepository.delete(cliente);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("O cliente ainda possui um agendamento!");
            }
            e.printStackTrace();
        }
    }

    public void validar(Cliente cliente) {

        if (cliente.getNome() == null || cliente.getNome().trim().equals("")) {
            throw new RegraNegocioException("Informe um nome válido.");
        }

        if (cliente.getCpf() == null || cliente.getCpf().trim().equals("")) {
            throw new RegraNegocioException("Informe um CPF válido.");
        }

        if (cliente.getDataNascimento() == null) {
            throw new RegraNegocioException("Informe uma data de nascimento válida.");
        }

        if (cliente.getDataNascimento().compareTo(new Date()) > 0) {
            throw new RegraNegocioException("Informe uma data de nascimento válida.");
        }

        if (cliente.getCep() == null || cliente.getCep().trim().equals("")) {
            throw new RegraNegocioException("Informe um CEP válido.");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Informe um telefone válido.");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Informe um email válido.");
        }

        if (cliente.getLogradouro() == null || cliente.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Informe um logradouro válido.");
        }

        if (cliente.getNumero() == null) {
            throw new RegraNegocioException("Informe um número de endereço válido.");
        }

        if (cliente.getBairro() == null || cliente.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Informe um bairro válido.");
        }

        if (cliente.getCidade() == null || cliente.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Informe uma cidade válida.");
        }

        if (cliente.getUf() == null || cliente.getUf().trim().equals("")) {
            throw new RegraNegocioException("Informe um estado válido.");
        }

    }
}
