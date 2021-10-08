package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.controller.dto.request.ItemVendaDTO;
import br.com.clinipet.ClinipetControl.controller.dto.request.VendaDTO;
import br.com.clinipet.ClinipetControl.controller.mapper.AgendamentoMapper;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.ItemVenda;
import br.com.clinipet.ClinipetControl.model.entity.Venda;
import br.com.clinipet.ClinipetControl.model.enums.StatusVendaEnum;
import br.com.clinipet.ClinipetControl.model.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;

    private final AgendamentoService agendamentoService;

    private final AgendamentoMapper agendamentoMapper;

    @Transactional
    public Venda efetuarVenda(VendaDTO vendaDTO) {

        List<ItemVenda> itemList = new ArrayList<>();

        vendaDTO.getItensVendaDTO().forEach(itemVendaDTO -> {

            Agendamento agendamento = agendamentoMapper.toEntity(itemVendaDTO.getAgendamento());
            Agendamento agendamentoSalvo = agendamentoService.marcar(agendamento);
            itemList.add(ItemVenda.builder().agendamento(agendamentoSalvo).quantidade(itemVendaDTO.getQuantidade()).build());
        });

        Venda venda = Venda.builder().itensVenda(itemList).dataCriacao(Date.valueOf(LocalDate.now())).status(StatusVendaEnum.PENDENTE).build();

        venda.getItensVenda().forEach(itemVenda -> itemVenda.setVenda(venda));

        return vendaRepository.save(venda);
    }

}
