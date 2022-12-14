package com.api.mercado.contollers;

import com.api.mercado.dtos.ProdutoDto;
import com.api.mercado.models.ProdutoModel;
import com.api.mercado.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Object> salvarProduto(@RequestBody @Valid ProdutoDto produtoDto){
        if(produtoService.existsByNomeProdutoAndFabricanteProduto(produtoDto.getNomeProduto(), produtoDto.getFabricanteProduto())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Produto já existe.");
        }
        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvarProduto(produtoModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editarProduto(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProdutoDto produtoDto){
        Optional <ProdutoModel> produtoModelOptional = produtoService.buscarProduto(id);
        if(produtoModelOptional.isPresent()){
            ProdutoModel produtoModel = new ProdutoModel();
            BeanUtils.copyProperties(produtoDto, produtoModel);

            produtoModel.setId(produtoModelOptional.get().getId());

            return ResponseEntity.status(HttpStatus.OK).body(produtoService.salvarProduto(produtoModel));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não existe.");

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable(value = "id") UUID id){
        Optional<ProdutoModel> produtoModelOptional = produtoService.buscarProduto(id);
        if(produtoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(produtoModelOptional);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado!");
    }

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> listarProdutos(){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.listarProdutos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarPorId(@PathVariable(value = "id") UUID id){
        Optional<ProdutoModel> produtoModelOptional = produtoService.buscarProduto(id);
        if(produtoModelOptional.isPresent()){
            produtoService.deletarProduto(id);
            return ResponseEntity.status(HttpStatus.OK).body(produtoModelOptional);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado!");
    }

}