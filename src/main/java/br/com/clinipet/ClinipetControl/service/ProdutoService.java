package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.controller.dto.response.ProdutoVendidoResponse;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.ItemVenda;
import br.com.clinipet.ClinipetControl.model.entity.Produto;
import br.com.clinipet.ClinipetControl.model.enums.StatusEstoqueEnum;
import br.com.clinipet.ClinipetControl.model.repository.ItemVendaRepository;
import br.com.clinipet.ClinipetControl.model.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ItemVendaRepository itemVendaRepository;

    @Transactional
    public Produto cadastrar(Produto produto) {
        //validar(servico);
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Produto produto) {
        Objects.requireNonNull(produto.getId());
        //validar(servico);
        return produtoRepository.save(produto);
    }


    @Transactional
    public void deletar(Produto produto) {
        Objects.requireNonNull(produto.getId());
        try {
            produtoRepository.delete(produto);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new RegraNegocioException("Existe uma venda com este serviço!");
            }
            e.printStackTrace();
        }
    }

    @Transactional
    public Produto baixaEstoque(Long id, Long quantidade) {
        Objects.requireNonNull(id);

        Produto produto = obterPorId(id).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        produto.baixaEstoque(quantidade);

        return produtoRepository.save(produto);

    }

    @Transactional
    public Produto entradaEstoque(Long id, Long quantidade) {
        Objects.requireNonNull(id);

        Produto produto = obterPorId(id).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        produto.entradaEstoque(quantidade);

        return produtoRepository.save(produto);

    }

    public List<ProdutoVendidoResponse> listarQuantidadeVendaProduto() {

        List<ProdutoVendidoResponse> produtoVendidoResponseList = new ArrayList<>();

        listarProdutos().forEach(produto -> {
            AtomicReference<Long> quantidade = new AtomicReference<>();
            quantidade.set(0L);

            List<ItemVenda> itemVendas = itemVendaRepository
                    .findByProduto(produto).stream().filter(p -> p.getProduto().getAtivo()).collect(Collectors.toList());
            itemVendas.forEach(itemVenda -> {
                quantidade.updateAndGet(v -> v + itemVenda.getQuantidade());
            });

            if (!(quantidade.get() == 0L)) {
                ProdutoVendidoResponse produtoVendidoResponse = ProdutoVendidoResponse.builder()
                        .label(produto.getNome())
                        .valorTotal(quantidade.get() * produto.getValorItem())
                        .value(quantidade.get()).build();
                produtoVendidoResponseList.add(produtoVendidoResponse);
            }
        });

        return produtoVendidoResponseList;
    }


    public List<Produto> listarProdutos() {
        return produtoRepository.findAllByAtivoTrue();
    }


    public Optional<Produto> obterPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public StatusEstoqueEnum getStatusEstoque(Long id) {
        Produto produto = obterPorId(id).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        return produto.statusEstoque();
    }

    public List<Produto> obterProdutoPorNome(String busca) {
        return produtoRepository.findByNomeOrMarca(busca);
    }

    public List<Produto> obterProdutoPorNomeComEstoque(String busca) {
        return produtoRepository.findByNomeOrMarcaComEstoque(busca);
    }

    public BigDecimal obterValorVenda(Long id) {
        return obterPorId(id).map(Produto::getValorItem)
                .map(BigDecimal::valueOf)
                .orElseThrow(() -> new RegraNegocioException("Produto não existente!"));
    }

}
