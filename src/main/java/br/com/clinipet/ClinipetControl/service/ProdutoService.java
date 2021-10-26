package br.com.clinipet.ClinipetControl.service;

import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Produto;
import br.com.clinipet.ClinipetControl.model.enums.StatusEstoqueEnum;
import br.com.clinipet.ClinipetControl.model.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

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
    public Produto baixaEstoque(Long id) {
        Objects.requireNonNull(id);

        Produto produto = obterPorId(id).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        produto.baixaEstoque();

        return produtoRepository.save(produto);


    }

    @Transactional
    public Produto entradaEstoque(Long id) {
        Objects.requireNonNull(id);

        Produto produto = obterPorId(id).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        produto.entradaEstoque();

        return produtoRepository.save(produto);


    }


    public Optional<Produto> obterPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public StatusEstoqueEnum getStatusEstoque(Long id) {
        Produto produto = obterPorId(id).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        return produto.statusEstoque();
    }


}
