package com.mtz.vendasapi.domain.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Pedido;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@Getter
@Setter
public class PedidoDTO extends RepresentationModel<PedidoDTO> {

	private Long id;

	@NotNull(message = "O Id do cliente é obrigatório.")
	private Long idCliente;

	@NotNull(message = "O Id do atendente é obrigatório.")
	private Long idAtendente;

	@NotNull(message = "A data do pedido é obrigatória.")
	private Date dataPedido;

	@NotNull(message = "O valor total é obrigatório.")
	@Positive(message = "O valor total deve ser maior que zero.")
	private BigDecimal valorTotal;

	private Set<ProdutoDTO> produtos;

	public PedidoDTO() {
	}

	public PedidoDTO(Pedido pedido) {
		this.id = pedido.getId();
		this.idCliente = pedido.getCliente().getId();
		this.idAtendente = pedido.getAtendente().getId();
		this.dataPedido = pedido.getDataPedido();
		this.valorTotal = pedido.getValorTotal();

		Set<ProdutoDTO> produtosAux = new HashSet<>();
		for (Produto produto : pedido.getProdutos()) {

			ProdutoDTO produtoDTO = new ProdutoDTO(produto);
			produtosAux.add(produtoDTO);

		}
		this.produtos = produtosAux;
	}

	public PedidoDTO(Long idAtendente, Long idCliente, Date dataPedido, BigDecimal valorTotal) {
		this.idCliente = idCliente;
		this.idAtendente = idAtendente;
		this.dataPedido = dataPedido;
		this.valorTotal = valorTotal;
	}

	public Pedido toEntity() {
		Pedido pedido = new Pedido();
		pedido.setId(id);
		pedido.setDataPedido(dataPedido);
		pedido.setValorTotal(valorTotal);
		Cliente cliente = new Cliente();
		cliente.setId(idCliente);
		pedido.setCliente(cliente);
		Usuario atendente = new Usuario();
		atendente.setId(idAtendente);
		pedido.setAtendente(atendente);

		Set<Produto> produtosAux = new HashSet<>();

		for (ProdutoDTO produtoDTO : produtos) {

			Produto produto = new Produto();
			produto.setId(produtoDTO.getId());

			produtosAux.add(produto);
		}

		pedido.setProdutos(produtosAux);
		return pedido;
	}
}
