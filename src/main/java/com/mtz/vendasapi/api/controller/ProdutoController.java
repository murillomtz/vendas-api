package com.mtz.vendasapi.api.controller;

import com.mtz.vendasapi.config.SwaggerConfig;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.model.Response;
import com.mtz.vendasapi.api.model.dto.ProdutoDTO;
import com.mtz.vendasapi.domain.service.IProdutoService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = SwaggerConfig.PRODUTO)
@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {


    private final IProdutoService produtoService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProdutoController(IProdutoService produtoService, ModelMapper modelMapper) {
        this.produtoService = produtoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Response<Page<ProdutoDTO>>> listar(
            @RequestParam(value = "filtro", required = false) String filtro,
            @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<ProdutoDTO> produtos = produtoService.listar(filtro, ordenacao, pagina, size);

        Response<Page<ProdutoDTO>> response = new Response<>();
        response.setContent(produtos.getContent());
        response.setStatusCode(HttpStatus.OK.value());
        response.setSize(produtos.getSize());
        response.setPageNumber(produtos.getPageable().getPageNumber());
        response.setTotalPages(produtos.getTotalPages());
        response.setTotalElementes(produtos.getTotalElements());
        response.setNumberOfElements(produtos.getNumberOfElements());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .criar(produtos.getContent().get(0))).withRel("Criar novo Produto: "));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<ProdutoDTO>> criar(@RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO produtoCriado = produtoService.criar(modelMapper.map(produtoDTO, Produto.class));

        Response<ProdutoDTO> response = new Response<>();
        response.setData(produtoCriado);
        response.setStatusCode(HttpStatus.CREATED.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .buscarPorId(produtoCriado.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/produtos")
                .withRel("Buscar todos os Produtos: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .atualizar(produtoCriado.getId(), produtoCriado)).withRel("Atualizar Produto: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .deletar(produtoCriado.getId())).withRel("Remover Produto: "));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProdutoDTO>> buscarPorId(@PathVariable Long id) {
        Response<ProdutoDTO> response = new Response<>();
        ProdutoDTO produtoDTO = this.produtoService.buscarPorId(id);
        Produto produto = modelMapper.map(produtoDTO, Produto.class);
        response.setData(produtoDTO);
        response.setStatusCode(HttpStatus.OK.value());

        if (produto != null) {

            response.add(Link.of("http://localhost:8080/produtos")
                    .withRel("Buscar todos os Produtos: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                    .atualizar(produto.getId(), produtoDTO)).withRel("Atualizar Produto: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                    .criar(produtoDTO)).withRel("Criar novo Produto: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                    .deletar(produto.getId())).withRel("Remover Produto: "));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ProdutoDTO>> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO) {


        Response<ProdutoDTO> response = new Response<>();
        Produto produto = modelMapper.map(produtoDTO, Produto.class);
        produto.setId(id);
        response.setData(this.produtoService.atualizar(produto));
        response.setStatusCode(HttpStatus.OK.value());
        ProdutoDTO produtoDTOChanger = modelMapper.map(produto, ProdutoDTO.class);

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .buscarPorId(produtoDTOChanger.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/produtos")
                .withRel("Buscar todos os Produtos: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .criar(produtoDTOChanger)).withRel("Criar novo Produto: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .deletar(produto.getId())).withRel("Remover Produto: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable Long id) {
        Response<String> response = new Response<>();
        response.setData(String.valueOf(produtoService.excluir(id)));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(Link.of("http://localhost:8080/produtos")
                .withRel("Buscar todos os Produtos: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/adicionarQuantidade")
    public ResponseEntity<Response<ProdutoDTO>> adicionarQuantidade(@PathVariable Long id, @RequestParam Integer quantidade) {

        Response<ProdutoDTO> response = new Response<>();
        Produto produto = modelMapper.map(this.produtoService.buscarPorId(id), Produto.class);
        //produto.setId(id);
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        response.setData(this.produtoService.atualizar(produto));
        response.setStatusCode(HttpStatus.OK.value());
        ProdutoDTO produtoDTOChanger = modelMapper.map(produto, ProdutoDTO.class);

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .buscarPorId(produto.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/produtos")
                .withRel("Buscar todos os Produtos: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .criar(modelMapper.map(produto, ProdutoDTO.class))).withRel("Criar novo Produto: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .deletar(produto.getId())).withRel("Remover Produto: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/removerQuantidade")
    public ResponseEntity<Response<ProdutoDTO>> removerQuantidade(@PathVariable Long id, @RequestParam Integer quantidade) {

        Response<ProdutoDTO> response = new Response<>();
        Produto produto = modelMapper.map(this.produtoService.buscarPorId(id), Produto.class);
        //produto.setId(id);
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        response.setData(this.produtoService.atualizar(produto));
        response.setStatusCode(HttpStatus.OK.value());
        ProdutoDTO produtoDTOChanger = modelMapper.map(produto, ProdutoDTO.class);

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .buscarPorId(produto.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/produtos")
                .withRel("Buscar todos os Produtos: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .criar(modelMapper.map(produto, ProdutoDTO.class))).withRel("Criar novo Produto: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class)
                .deletar(produto.getId())).withRel("Remover Produto: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
