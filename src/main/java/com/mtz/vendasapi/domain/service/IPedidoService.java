package com.mtz.vendasapi.domain.service;

import com.mtz.vendasapi.domain.model.dto.PedidoDTO;
import org.springframework.data.domain.Page;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Pedido;

public interface IPedidoService {

	public Page<PedidoDTO> listar(String filtro, String ordenacao, int pagina);

	public PedidoDTO criar(Long idCliente, Pedido pedido);

	public PedidoDTO buscarPorId(Long id) throws NegocioException;

	public PedidoDTO atualizar(Pedido pedido, Boolean isAtualiaza);

	public String excluir(Long id);

	public Page<PedidoDTO> findPedidosByProduto(Long id, String filtro, String ordenacao, int pagina);

}