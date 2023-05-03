package com.mtz.vendasapi.domain.service.serviceImp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mtz.vendasapi.domain.constant.MensagensConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Pedido;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.repository.ClienteRepository;
import com.mtz.vendasapi.domain.repository.PedidoRepository;
import com.mtz.vendasapi.domain.repository.ProdutoRepository;
import com.mtz.vendasapi.domain.service.IPedidoService;
import com.mtz.vendasapi.infrastructure.PedidoSpecs;

@Service
public class PedidoServiceImp implements IPedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Page<Pedido> listar(String filtro, String ordenacao, int pagina) throws NegocioException {
        try {
            Pageable pageable = PageRequest.of(pagina, 10, Sort.by(ordenacao));
            return pedidoRepository.findAll(PedidoSpecs.filtrarPor(filtro), pageable);
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Pedido criar(Long idCliente, Pedido pedido) throws NegocioException {
        try {
            Cliente cliente = clienteRepository.findById(idCliente)
                    .orElseThrow(() -> new NegocioException(MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND));
            pedido.setCliente(cliente);

            Set<Produto> produtos = new HashSet<>();

            for (Produto produto : pedido.getProdutos()) {

                if (produto.getId() != null) {
                    Produto produtoAux = produtoRepository.findById(produto.getId())
                            .orElseThrow(() -> new NegocioException(MensagensConstant.ERRO_PRODUTO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND));

                    produtos.add(produtoAux);
                }
            }
            pedido.setProdutos(produtos);
            return pedidoRepository.save(pedido);
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Pedido buscarPorId(Long id) throws NegocioException {
        try {
            return pedidoRepository.findById(id).orElseThrow(() -> new NegocioException(MensagensConstant.ERRO_PEDIDO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void atualizar(Pedido pedido, Boolean isAtualiaza) throws NegocioException {
        try {
            Pedido pedidoExistente = buscarPorId(pedido.getId());

            for (Produto produto : pedido.getProdutos()) {
                if (produto.getId() != null) {
                    if (isAtualiaza == Boolean.TRUE) {
                        pedidoExistente.getProdutos().clear();
                    }

                    pedidoExistente.getProdutos().add(produtoRepository.findById(produto.getId())
                            .orElseThrow(() ->
                                    new NegocioException(MensagensConstant.ERRO_PRODUTO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND)));
                }
            }
            pedidoExistente.setCliente(pedido.getCliente());
            pedidoExistente.setAtendente(pedido.getAtendente());
            pedidoExistente.setDataPedido(pedido.getDataPedido());
            pedidoExistente.setValorTotal(pedido.getValorTotal());

            pedidoRepository.save(pedidoExistente);
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> excluir(Long id) {
        try {
            pedidoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Pedido excluido com sucesso.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        } catch (DataIntegrityViolationException i) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esse Pedido não pode ser excluido.");
        }
    }

    @Override
    public Page<Pedido> findPedidosByProduto(Long id, String filtro, String ordenacao, int pagina) {
        try {
            Pageable pageable = PageRequest.of(pagina, 10, Sort.by(ordenacao));
            List<Pedido> pedidos = pedidoRepository.findByProdutoId(id);
            return new PageImpl<Pedido>(pedidos, pageable, pedidos.size());
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
