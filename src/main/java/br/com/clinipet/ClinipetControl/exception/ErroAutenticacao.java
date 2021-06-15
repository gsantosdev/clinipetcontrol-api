package br.com.clinipet.ClinipetControl.exception;

public class ErroAutenticacao extends RuntimeException {
    public ErroAutenticacao(String msg) {
        super(msg);
    }
}
