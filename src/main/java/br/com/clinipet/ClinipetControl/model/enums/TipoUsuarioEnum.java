package br.com.clinipet.ClinipetControl.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TipoUsuarioEnum {
    VETERINARIO,
    SECRETARIA,
    ADMIN;


    public static List<TipoUsuarioEnum> getTipos() {
        return Arrays.asList(TipoUsuarioEnum.values());
    }

    public static List<Map<String, String>> listarNomesTipos() {

        List<Map<String, String>> listaNomes = new ArrayList<>();
        getTipos().forEach(tipo -> {
            Map<String, String> nomesTipos = new HashMap<>();
            nomesTipos.put("label", tipo.name());
            listaNomes.add(nomesTipos);
        });

        return listaNomes;
    }
}
