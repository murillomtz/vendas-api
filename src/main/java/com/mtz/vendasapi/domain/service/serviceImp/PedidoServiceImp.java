package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.api.controller.PedidoController;
import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Pedido;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.api.model.dto.PedidoDTO;
import com.mtz.vendasapi.api.model.dto.ProdutoDTO;
import com.mtz.vendasapi.domain.repository.ClienteRepository;
import com.mtz.vendasapi.domain.repository.PedidoRepository;
import com.mtz.vendasapi.domain.repository.ProdutoRepository;
import com.mtz.vendasapi.domain.service.IPedidoService;
import com.mtz.vendasapi.infrastructure.PedidoSpecs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PedidoServiceImp implements IPedidoService {

    //Autowired
    private final PedidoRepository pedidoRepository;

    //@Autowired
    private final ProdutoRepository produtoRepository;

    //Autowired
    private final ClienteRepository clienteRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public PedidoServiceImp(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<PedidoDTO> listar(String filtro, String ordenacao, int pagina) {

        try {
            Page<PedidoDTO> pedidosDTO = this.pedidoRepository.findAll(PedidoSpecs.
                            filtrarPor(filtro), PageRequest.of(pagina, 10, Sort.by(ordenacao))).

                    map(pedido -> modelMapper.map(pedido, PedidoDTO.class));

            pedidosDTO.forEach(pedido -> pedido.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).
                    buscarPorId(pedido.getId())).withRel("Buscar Pelo ID: ")));

            return pedidosDTO;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PedidoDTO criar(Long idCliente, Pedido pedido) {
        try {
            if (pedido.getId() != null) {
                throw new NegocioException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }

            Cliente cliente = this.clienteRepository.findById(idCliente)
                    .orElseThrow(() -> new NegocioException(MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND));
            pedido.setCliente(cliente);

            Set<Produto> pradutosAux = new HashSet<>();

            List<BigDecimal> valorPedidos = new ArrayList<>();
            pedido.getProdutos().forEach(produto -> {
                Produto produtoById = this.produtoRepository.findById(produto.getId())
                        .orElseThrow(() ->
                                new NegocioException(MensagensConstant.ERRO_PRODUTO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND));
                pradutosAux.add(produtoById);
                valorPedidos.add(produtoById.getValorVenda().multiply(new BigDecimal(produtoById.getQuantidade())));
            });
            pedido.setProdutos(pradutosAux);
            BigDecimal valorPedido = valorPedidos.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            pedido.setValorTotal(pedido.getValorTotal().add(valorPedido));
            return this.cadastrarOuAtualizar(pedido);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PedidoDTO buscarPorId(Long id) {

        try {
            Optional<Pedido> pedidoOptional = this.pedidoRepository.findById(id);
            if (pedidoOptional.isPresent()) {
                return modelMapper.map(pedidoOptional.get(), PedidoDTO.class);
            }
            throw new NegocioException(MensagensConstant.ERRO_PEDIDO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PedidoDTO atualizar(Pedido pedido, Boolean isAtualiaza) {
        try {
            PedidoDTO pedidoExistente = modelMapper.map(pedido, PedidoDTO.class);

            if (isAtualiaza == Boolean.TRUE) {
                pedidoExistente.getProdutos().clear();
            }
            pedido.getProdutos().forEach(produto -> {
                        if (produto.getId() != null) {

                            pedidoExistente.getProdutos().add(modelMapper.map(produto, ProdutoDTO.class));
                        }
                    }

            );
            pedidoExistente.setValorTotal(pedido.getValorTotal());
            pedidoExistente.setIdCliente(pedido.getCliente().getId());
            pedidoExistente.setIdAtendente(pedido.getAtendente().getId());
            pedidoExistente.setDataPedido(pedido.getDataPedido());


            return this.cadastrarOuAtualizar(modelMapper.map(pedidoExistente, Pedido.class));
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String excluir(Long id) {
        try {
            this.pedidoRepository.deleteById(id);
            return MensagensConstant.OK_EXCLUIDO_COM_SUCESSO.getValor();
        } catch (EmptyResultDataAccessException e) {
            return MensagensConstant.ERRO_PEDIDO_NAO_ENCONTRADO.getValor();
        } catch (DataIntegrityViolationException i) {
            return MensagensConstant.ERRO_EXCLUIR_PEDIDO.getValor();
        }

    }

    @Override
    public Page<PedidoDTO> findPedidosByProduto(Long id, String filtro, String ordenacao, int pagina) {
        try {
            Pageable pageable = PageRequest.of(pagina, 10, Sort.by(ordenacao));
            List<PedidoDTO> pedidosDTO = this.pedidoRepository.findByProdutoId(id);
            if (pedidosDTO.isEmpty()) {
                throw new NegocioException("Não existe Pedidos com o produto selecionado.", HttpStatus.NOT_FOUND);
            }

            pedidosDTO.forEach(pedido -> pedido.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).
                    buscarPorId(pedido.getId())).withRel("Buscar Pelo ID: ")));

            return new PageImpl<PedidoDTO>(pedidosDTO, pageable, pedidosDTO.size());
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PedidoDTO cadastrarOuAtualizar(Pedido pedido) {
        Pedido pedidoEntity = this.pedidoRepository.save(pedido);
        PedidoDTO pedidoDTO = modelMapper.map(pedidoEntity, PedidoDTO.class);
        return pedidoDTO;
    }

    @Override
    public Page<PedidoDTO> findPedidosByIdCliente(Long idCliente, String filtro, String ordenacao, int pagina) {
        try {
            Pageable pageable = PageRequest.of(pagina, 10, Sort.by(ordenacao));
            List<PedidoDTO> pedidosDTO = pedidoRepository.findByClienteId(idCliente);

            if (pedidosDTO.isEmpty()) {
                throw new NegocioException("Não existem pedidos para o cliente selecionado.", HttpStatus.NOT_FOUND);
            }

            pedidosDTO.forEach(pedido -> pedido.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class)
                    .buscarPorId(pedido.getId())).withRel("Buscar Pelo ID")));

            return new PageImpl<>(pedidosDTO, pageable, pedidosDTO.size());
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
