package com.api.mercado.services;

import com.api.mercado.models.CarrinhoModel;
import com.api.mercado.models.ProdutoModel;
import com.api.mercado.repositories.CarrinhoRepository;
import com.api.mercado.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrinhoService {

    @Autowired
    ProdutoRepository produtoRepository;

    final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository){
        this.carrinhoRepository = carrinhoRepository;
    }

    @Transactional
    public CarrinhoModel criarCarrinho(){
        CarrinhoModel carrinhoModel = new CarrinhoModel();
        carrinhoModel.setValorTotalCarrinho(BigDecimal.ZERO);
        carrinhoModel.setProdutos(new ArrayList<>());
        carrinhoModel.setCarrinhoFechado(false);
        return carrinhoRepository.save(carrinhoModel);
    }

    public Optional<CarrinhoModel> buscarCarrinho(UUID id){return carrinhoRepository.findById(id);}

    public List<CarrinhoModel> buscarCarrinhos(){
        return carrinhoRepository.findAll();
    }

    @Transactional
    public void deletarCarrinho(UUID id){
        carrinhoRepository.deleteById(id);
    }

    @Transactional
    public Optional<CarrinhoModel> adicionarProdutos(@NotNull CarrinhoModel carrinhoModel, UUID idProduto, int quantidade){
        Optional<ProdutoModel> produtoModelOptional = produtoRepository.findById(idProduto);
        if(produtoModelOptional.isEmpty()){
            return null;
        }
        carrinhoModel.adicionarProduto(produtoModelOptional.get(), quantidade);
        produtoModelOptional.get().setQuantidadeEstoque(produtoModelOptional.get().getQuantidadeEstoque() - quantidade);
        return Optional.of(carrinhoRepository.save(carrinhoModel));
    }

}
