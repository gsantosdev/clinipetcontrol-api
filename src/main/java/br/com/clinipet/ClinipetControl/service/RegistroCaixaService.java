package br.com.clinipet.ClinipetControl.service;


import br.com.clinipet.ClinipetControl.model.entity.RegistroCaixa;
import br.com.clinipet.ClinipetControl.model.repository.RegistroCaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistroCaixaService {

    private final RegistroCaixaRepository registroCaixaRepository;

    public RegistroCaixa salvar(RegistroCaixa registroCaixa) {
        return registroCaixaRepository.save(registroCaixa);
    }
}
