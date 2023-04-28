package com.mtz.vendasapi.api.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.mtz.vendasapi.domain.model.Pedido;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.model.dto.PedidoDTO;
import com.mtz.vendasapi.domain.model.dto.ProdutoDTO;
import com.mtz.vendasapi.domain.service.IClienteService;
import com.mtz.vendasapi.domain.service.IPedidoService;
import com.mtz.vendasapi.domain.service.IProdutoService;
import com.mtz.vendasapi.domain.service.IUsuarioService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private IPedidoService pedidoService;

	@Autowired
	private IProdutoService produtoService;

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<Page<PedidoDTO>> listar(@RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
			@RequestParam(value = "pagina", defaultValue = "0") int pagina) {

		Page<PedidoDTO> pedidos = pedidoService.listar(filtro, ordenacao, pagina).map(pedido -> new PedidoDTO(pedido));

		return ResponseEntity.ok(pedidos);
	}

	@PostMapping
	public ResponseEntity<PedidoDTO> criar(@RequestBody PedidoDTO pedidoDTO) {

		Pedido pedido = pedidoService.criar(pedidoDTO.getIdCliente(), pedidoDTO.toEntity());
		return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoDTO(pedido));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Long id) {
		Pedido pedido;
		try {
			pedido = pedidoService.buscarPorId(id);
		} catch (NegocioException e) {
			return ResponseEntity.notFound().build();
		}
		PedidoDTO pedidoDTO = new PedidoDTO(pedido);
		return ResponseEntity.ok(pedidoDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PedidoDTO> atualizar(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
		try {
			Pedido pedidoExistente = pedidoService.buscarPorId(id);
			if (pedidoExistente != null) {
				Pedido pedido = pedidoDTO.toEntity();
				pedido.setId(id);
				pedido.setAtendente(usuarioService.buscarPorId(pedidoDTO.getIdAtendente()));
				pedido.setCliente(clienteService.buscarPorId(pedidoDTO.getIdCliente()));
				pedido.setDataPedido(pedidoDTO.getDataPedido());
				pedido.setValorTotal(pedidoDTO.getValorTotal());

				pedidoService.atualizar(pedido, Boolean.TRUE);
				return ResponseEntity.ok(new PedidoDTO(pedido));
			}
			return ResponseEntity.notFound().build();
		} catch (NegocioException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> excluir(@PathVariable Long id) {
		return pedidoService.excluir(id);
	}

	@PostMapping("/{id}/produtos")
	public ResponseEntity<PedidoDTO> adicionarProdutos(@PathVariable Long id, @RequestBody List<ProdutoDTO> produtos) {
		try {
			Pedido pedido = pedidoService.buscarPorId(id);
			if (pedido != null) {
				// Set<Produto> listaProdutos =
				// produtos.stream().map(ProdutoDTO::toEntity).collect(Collectors.toSet());
				Set<Produto> listaProdutos = new HashSet<>();

				produtos.forEach(produto -> {
					listaProdutos.add(produtoService.buscarPorId(produto.getId()));
				});

				pedido.setProdutos(listaProdutos);
				pedidoService.atualizar(pedido, Boolean.FALSE);
				return ResponseEntity.ok(new PedidoDTO(pedido));
			}
			return ResponseEntity.notFound().build();
		} catch (NegocioException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/produtos/{id}")
	public ResponseEntity<Page<PedidoDTO>> getPedidosByProduto(@PathVariable Long id,
			@RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
			@RequestParam(value = "pagina", defaultValue = "0") int pagina) {

		Page<Pedido> pedidos = pedidoService.findPedidosByProduto(id, filtro, ordenacao, pagina);
		Page<PedidoDTO> pedidosDTO = pedidos.map(pedido -> new PedidoDTO(pedido));

		return ResponseEntity.ok(pedidosDTO);
	}

}
