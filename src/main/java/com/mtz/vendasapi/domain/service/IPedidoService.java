package com.mtz.vendasapi.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Pedido;

public interface IPedidoService {

	public Page<Pedido> listar(String filtro, String ordenacao, int pagina) throws NegocioException;

	public Pedido criar(Long idCliente, Pedido pedido) throws NegocioException;

	public Pedido buscarPorId(Long id) throws NegocioException;

	public void atualizar(Pedido pedido, Boolean isAtualiaza) throws NegocioException;

	public ResponseEntity<String> excluir(Long id);

	public Page<Pedido> findPedidosByProduto(Long id, String filtro, String ordenacao, int pagina);

}