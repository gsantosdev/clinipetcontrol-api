package br.com.clinipet.ClinipetControl.model.repository;


import br.com.clinipet.ClinipetControl.model.entity.ItemVenda;
import br.com.clinipet.ClinipetControl.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    public List<ItemVenda> findByProduto(Produto produto);

}
