package com.mtz.vendasapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.model.dto.ProdutoDTO;
import com.mtz.vendasapi.domain.service.IProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private IProdutoService produtoService;

	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> listar(@RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
			@RequestParam(value = "pagina", defaultValue = "0") int pagina) {
		Page<ProdutoDTO> produtos = produtoService.listar(filtro, ordenacao, pagina)
				.map(produto -> new ProdutoDTO(produto));
		return ResponseEntity.ok(produtos);
	}

	@PostMapping
	public ResponseEntity<ProdutoDTO> criar(@RequestBody ProdutoDTO produtoDTO) {
		Produto produto = produtoService.criar(produtoDTO.toEntity());
		return ResponseEntity.status(HttpStatus.CREATED).body(new ProdutoDTO(produto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
		Produto produto;
		try {
			produto = produtoService.buscarPorId(id);
		} catch (NegocioException e) {
			return ResponseEntity.notFound().build();
		}
		ProdutoDTO produtoDTO = new ProdutoDTO(produto);
		return ResponseEntity.ok(produtoDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO) {
		try {
			Produto produtoExistente = produtoService.buscarPorId(id);
			if (produtoExistente != null) {
				Produto produto = produtoDTO.toEntity();
				produto.setId(id);

				produtoService.atualizar(produto);
				return ResponseEntity.ok(new ProdutoDTO(produto));
			}
			return ResponseEntity.notFound().build();
		} catch (NegocioException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletar(@PathVariable Long id) {

		return produtoService.excluir(id);

	}

}
