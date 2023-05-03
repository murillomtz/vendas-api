package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.domain.constant.MensagensConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.repository.ProdutoRepository;
import com.mtz.vendasapi.domain.service.IProdutoService;
import com.mtz.vendasapi.infrastructure.ProdutoSpecs;

@Service
public class ProdutoServiceImp implements IProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Override
	public Page<Produto> listar(String filtro, String ordenacao, int pagina) throws NegocioException {
		try {
			Pageable pageable = PageRequest.of(pagina, 10, Sort.by(ordenacao));
			return produtoRepository.findAll(ProdutoSpecs.filtrarPor(filtro), pageable);
		} catch (Exception e) {
			throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Produto criar(Produto produto) throws NegocioException {
		try {
			return produtoRepository.save(produto);
		} catch (Exception e) {
			throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Produto buscarPorId(Long id) throws NegocioException {
		try {
			return produtoRepository.findById(id).orElseThrow(()
					-> new NegocioException(MensagensConstant.ERRO_PRODUTO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND));
		} catch (Exception e) {
			throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void atualizar(Produto produto) throws NegocioException {
		try {
			Produto produtoExistente = buscarPorId(produto.getId());

			produtoExistente.setNome(produto.getNome());
			produtoExistente.setDescricao(produto.getDescricao());
			produtoExistente.setQuantidade(produto.getQuantidade());
			produtoExistente.setValorVenda(produto.getValorVenda());

			produtoRepository.save(produtoExistente);
		} catch (NegocioException e) {
			throw e;
		} catch (Exception e) {
			throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> excluir(Long id) {
		try {
			produtoRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Produto excluido com sucesso.");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
		} catch (DataIntegrityViolationException i) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Esse Produto está incluso em algum pedido, não é possivel excluir.");
		}
	}

}
