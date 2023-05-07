package com.mtz.vendasapi.api.controller;

import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Pedido;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.model.Response;
import com.mtz.vendasapi.domain.model.dto.PedidoDTO;
import com.mtz.vendasapi.domain.model.dto.ProdutoDTO;
import com.mtz.vendasapi.domain.service.IClienteService;
import com.mtz.vendasapi.domain.service.IPedidoService;
import com.mtz.vendasapi.domain.service.IProdutoService;
import com.mtz.vendasapi.domain.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Response<Page<PedidoDTO>>> listar(@RequestParam(value = "filtro", required = false) String filtro, @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao, @RequestParam(value = "pagina", defaultValue = "0") int pagina) {

        Response<Page<PedidoDTO>> response = new Response<>();
        Page<PedidoDTO> pedidos = this.pedidoService.listar(filtro, ordenacao, pagina);
        //response.setData(pedidos);
        response.setContent(pedidos.getContent());
        response.setStatusCode(HttpStatus.OK.value());
        response.setSize(pedidos.getSize());
        response.setPageNumber(pedidos.getPageable().getPageNumber());
        response.setTotalPages(pedidos.getTotalPages());
        response.setTotalElementes(pedidos.getTotalElements());
        response.setNumberOfElements(pedidos.getNumberOfElements());


        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).criar(pedidos.getContent().get(0))).withRel("Criar novo Pedido: "));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<PedidoDTO>> criar(@RequestBody PedidoDTO pedidoDTO) {

        Response<PedidoDTO> response = new Response<>();
        PedidoDTO pedidoDTO1 = this.pedidoService.criar(pedidoDTO.getIdCliente(), pedidoDTO.toEntity());
        response.setData(pedidoDTO1);
        response.setStatusCode(HttpStatus.CREATED.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).buscarPorId(pedidoDTO.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/clientes").withRel("Buscar todos os Pedidos: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).atualizar(pedidoDTO.getId(), pedidoDTO)).withRel("Atualizar Pedido: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).excluir(pedidoDTO.getId())).withRel("Remover Pedidos: "));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PedidoDTO>> buscarPorId(@PathVariable Long id) {
        Response<PedidoDTO> response = new Response<>();
        PedidoDTO pedidoDTO = this.pedidoService.buscarPorId(id);
        Pedido pedido = pedidoDTO.toEntity();
        response.setData(pedidoDTO);
        response.setStatusCode(HttpStatus.OK.value());


        if (pedido != null) {

            response.add(Link.of("http://localhost:8080/pedidos").withRel("Buscar todos os Pedidos: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).atualizar(pedido.getId(), pedidoDTO)).withRel("Atualizar Pedido: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).criar(pedidoDTO)).withRel("Criar novo Pedido: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).excluir(pedido.getId())).withRel("Remover Pedidos: "));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<PedidoDTO>> atualizar(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        Response<PedidoDTO> response = new Response<>();

        PedidoDTO pedidoExistente = pedidoService.buscarPorId(id);

        if (pedidoExistente != null) {
            Pedido pedido = pedidoExistente.toEntity();
            pedido.setAtendente(usuarioService.buscarPorId(pedidoDTO.getIdAtendente()).toEntity());
            pedido.setCliente(clienteService.buscarPorId(pedidoDTO.getIdCliente()).toEntity());
            pedido.setValorTotal(BigDecimal.ZERO);
            Set<Produto> produtosAux = new HashSet<>();
            List<BigDecimal> valorPedidos = new ArrayList<>();

            Produto produtoAux;

            for (ProdutoDTO produto : pedidoDTO.getProdutos()) {
                produtoAux = this.produtoService.buscarPorId(produto.getId()).toEntity();
                produtosAux.add(produtoAux);

                pedido.getValorTotal().add(produtoAux.getValorVenda());
                valorPedidos.add(produtoAux.getValorVenda().multiply(new BigDecimal(produtoAux.getQuantidade())));
            }

            BigDecimal valorPedido = valorPedidos.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            pedido.setValorTotal(valorPedido);
            pedido.setProdutos(produtosAux);
            response.setData(pedidoService.atualizar(pedido, Boolean.TRUE));
        }
        response.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> excluir(@PathVariable Long id) {
        //return pedidoService.excluir(id);

        Response<String> response = new Response<>();
        response.setData(String.valueOf(this.pedidoService.excluir(id)));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(Link.of("http://localhost:8080/pedidos")
                .withRel("Buscar todos os Clientes: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}/produtos")
    public ResponseEntity<Response<PedidoDTO>> adicionarProdutos(@PathVariable Long id, @RequestBody List<ProdutoDTO> produtos) {

        Response<PedidoDTO> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        PedidoDTO pedido = this.pedidoService.buscarPorId(id);

        if (pedido != null) {
            List<BigDecimal> valorPedidos = new ArrayList<>();
            produtos.forEach(produto -> {
                ProdutoDTO produtoById = this.produtoService.buscarPorId(produto.getId());
                pedido.getProdutos().add(produtoById);
                valorPedidos.add(produtoById.getValorVenda().multiply(new BigDecimal(produtoById.getQuantidade())));

            });
            BigDecimal valorPedido = valorPedidos.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            pedido.setValorTotal(pedido.getValorTotal().add(valorPedido));
            response.setData(this.pedidoService.atualizar(pedido.toEntity(), Boolean.FALSE));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @DeleteMapping("/{id}/produtos")
    public ResponseEntity<Response<PedidoDTO>> removerProdutos(@PathVariable Long id, @RequestBody List<ProdutoDTO> produtos) {
        Response<PedidoDTO> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        PedidoDTO pedido = pedidoService.buscarPorId(id);

        if (pedido != null) {
            for (ProdutoDTO produtoDTO : produtos) {
                // busca o produto na lista e o remove
                pedido.getProdutos().removeIf(produto -> produto.getId().equals(produtoDTO.getId()));
                ProdutoDTO produto = this.produtoService.buscarPorId(produtoDTO.getId());
                pedido.setValorTotal(pedido.getValorTotal().subtract(produto.getValorVenda().multiply(new BigDecimal(produto.getQuantidade()))));
            }
            response.setData(this.pedidoService.atualizar(pedido.toEntity(), Boolean.FALSE));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<Response<Page<PedidoDTO>>> getPedidosByProduto(@PathVariable Long id, @RequestParam(value = "filtro", required = false) String filtro, @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao, @RequestParam(value = "pagina", defaultValue = "0") int pagina) {

        Response<Page<PedidoDTO>> response = new Response<>();
        Page<PedidoDTO> pedidos = this.pedidoService.findPedidosByProduto(id, filtro, ordenacao, pagina);
        //response.setData(pedidos);
        response.setContent(pedidos.getContent());
        response.setStatusCode(HttpStatus.OK.value());
        response.setSize(pedidos.getSize());
        response.setPageNumber(pedidos.getPageable().getPageNumber());
        response.setTotalPages(pedidos.getTotalPages());
        response.setTotalElementes(pedidos.getTotalElements());
        response.setNumberOfElements(pedidos.getNumberOfElements());


        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).criar(pedidos.getContent().get(0))).withRel("Criar novo Pedido: "));

        return ResponseEntity.ok(response);
    }

}
