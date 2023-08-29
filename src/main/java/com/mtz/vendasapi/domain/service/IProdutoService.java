package com.mtz.vendasapi.domain.service;

import com.mtz.vendasapi.domain.model.dto.ProdutoDTO;
import org.springframework.data.domain.Page;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Produto;

public interface IProdutoService {

	public Page<ProdutoDTO> listar(String filtro, String ordenacao, int pagina, int size);

	public ProdutoDTO criar(Produto produto) throws NegocioException;

	public ProdutoDTO buscarPorId(Long id) throws NegocioException;

	public ProdutoDTO atualizar(Produto produto) throws NegocioException;

	public String excluir(Long id);
}