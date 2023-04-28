package com.mtz.vendasapi.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Produto;

public interface IProdutoService {

	public Page<Produto> listar(String filtro, String ordenacao, int pagina) throws NegocioException;

	public Produto criar(Produto produto) throws NegocioException;

	public Produto buscarPorId(Long id) throws NegocioException;

	public void atualizar(Produto produto) throws NegocioException;

	public ResponseEntity<String> excluir(Long id);
}