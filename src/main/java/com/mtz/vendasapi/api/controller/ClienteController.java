package com.mtz.vendasapi.api.controller;

import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Response;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;


    @GetMapping
    public ResponseEntity<Response<Page<ClienteDTO>>> listar(@RequestParam(value = "filtro", required = false) String filtro,
                                                             @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
                                                             @RequestParam(value = "pagina", defaultValue = "0") int pagina) {

        Response<Page<ClienteDTO>> response = new Response<>();
        Page<ClienteDTO> clientes = this.clienteService.listar(filtro, ordenacao, pagina);
        //response.setData(clientes);
        response.setContent(clientes.getContent());
        response.setStatusCode(HttpStatus.OK.value());
        response.setSize(clientes.getSize());
        response.setPageNumber(clientes.getPageable().getPageNumber());
        response.setTotalPages(clientes.getTotalPages());
        response.setTotalElementes(clientes.getTotalElements());
        response.setNumberOfElements(clientes.getNumberOfElements());


        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .criar(clientes.getContent().get(0))).withRel("Criar novo CLiente: "));

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ClienteDTO>> buscar(@PathVariable Long id) {

        Response<ClienteDTO> response = new Response<>();
        ClienteDTO clienteDTO = this.clienteService.buscarPorId(id);
        Cliente cliente = clienteDTO.toEntity();
        response.setData(clienteDTO);
        response.setStatusCode(HttpStatus.OK.value());

        if (cliente != null) {

            response.add(Link.of("http://localhost:8080/clientes")
                    .withRel("Buscar todos os Clientes: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .atualizar(cliente.getId(), clienteDTO)).withRel("Atualizar Cliente: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .criar(clienteDTO)).withRel("Criar novo CLiente: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .deletar(cliente.getId())).withRel("Remover Clientes: "));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Response<ClienteDTO>> criar(@RequestBody @Valid ClienteDTO clienteDTO) {

        Response<ClienteDTO> response = new Response<>();
        ClienteDTO clienteDTO1 = this.clienteService.criar(clienteDTO.toEntity());
        response.setData(clienteDTO1);
        response.setStatusCode(HttpStatus.CREATED.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .buscar(clienteDTO.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/clientes")
                .withRel("Buscar todos os Clientes: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .atualizar(clienteDTO.getId(), clienteDTO)).withRel("Atualizar Cliente: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .deletar(clienteDTO.getId())).withRel("Remover Clientes: "));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ClienteDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDTO) {

        Response<ClienteDTO> response = new Response<>();
        Cliente cliente = clienteDTO.toEntity();
        cliente.setId(id);
        response.setData(this.clienteService.atualizar(cliente));
        response.setStatusCode(HttpStatus.OK.value());
        ClienteDTO clienteDTOChenger = new ClienteDTO(cliente);

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .buscar(clienteDTO.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/clientes")
                .withRel("Buscar todos os Clientes: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .criar(clienteDTO)).withRel("Criar novo CLiente: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .deletar(cliente.getId())).withRel("Remover Clientes: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable Long id) {

        Response<String> response = new Response<>();
        response.setData(String.valueOf(this.clienteService.deletar(id).getBody()));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(Link.of("http://localhost:8080/clientes")
                .withRel("Buscar todos os Clientes: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
