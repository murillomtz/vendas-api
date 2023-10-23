package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.api.controller.ProdutoController;
import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.api.model.dto.ProdutoDTO;
import com.mtz.vendasapi.infrastructure.ProdutoSpecs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.repository.ProdutoRepository;
import com.mtz.vendasapi.domain.service.IProdutoService;

import java.util.Optional;

@Service
public class ProdutoServiceImp implements IProdutoService {


    private final ProdutoRepository produtoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProdutoServiceImp(ProdutoRepository produtoRepository, ModelMapper modelMapper) {
        this.produtoRepository = produtoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ProdutoDTO> listar(String filtro, String ordenacao, int pagina, int size) {

        try {
            Page<ProdutoDTO> produtoDTO = this.produtoRepository.findAll(ProdutoSpecs.
                            filtrarPor(filtro), PageRequest.of(pagina, size, Sort.by(ordenacao))).
                    map(produto -> modelMapper.map(produto, ProdutoDTO.class));

            produtoDTO.forEach(produto -> produto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class).
                    buscarPorId(produto.getId())).withRel("Buscar Pelo ID: ")));

            return produtoDTO;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProdutoDTO criar(Produto produto) {

        try {
            if (produto.getId() != null) {
                throw new NegocioException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }
            return this.cadastrarOuAtualizar(produto);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProdutoDTO buscarPorId(Long id) {

        try {
            Optional<Produto> produtoOptional = this.produtoRepository.findById(id);
            if (produtoOptional.isPresent()) {
                return modelMapper.map(produtoOptional.get(), ProdutoDTO.class);
            }
            throw new NegocioException(MensagensConstant.ERRO_PRODUTO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProdutoDTO atualizar(Produto produto) throws NegocioException {

        try {
            this.buscarPorId(produto.getId());
            return this.cadastrarOuAtualizar(produto);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String excluir(Long id) {
        try {
            this.produtoRepository.deleteById(id);
            return MensagensConstant.OK_EXCLUIDO_COM_SUCESSO.getValor();
        } catch (EmptyResultDataAccessException e) {
            return MensagensConstant.ERRO_PRODUTO_NAO_ENCONTRADO.getValor();
        } catch (DataIntegrityViolationException i) {
            return MensagensConstant.ERRO_EXCLUIR_PRODUTO.getValor();
        }

    }

    private ProdutoDTO cadastrarOuAtualizar(Produto produto) {
        Produto produtoEntity = this.produtoRepository.save(produto);
        ProdutoDTO produtoDTO = modelMapper.map(produtoEntity, ProdutoDTO.class);
        return produtoDTO;
    }

}
