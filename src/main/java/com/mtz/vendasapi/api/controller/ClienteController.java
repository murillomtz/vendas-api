package com.mtz.vendasapi.api.controller;

import com.mtz.vendasapi.config.SwaggerConfig;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Response;
import com.mtz.vendasapi.api.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.service.IClienteService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = SwaggerConfig.CLIENTE)
@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final IClienteService clienteService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClienteController(IClienteService clienteService, ModelMapper modelMapper) {
        this.clienteService = clienteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Response<Page<ClienteDTO>>> listar(@RequestParam(value = "filtro", required = false) String filtro,
                                                             @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
                                                             @RequestParam(value = "pagina", defaultValue = "0") int pagina) {

        Response<Page<ClienteDTO>> response = new Response<>();
        Page<ClienteDTO> clientes = this.clienteService.listar(filtro, ordenacao, pagina);

        response.setContent(clientes.getContent());
        response.setStatusCode(HttpStatus.OK.value());
        response.setSize(clientes.getSize());
        response.setPageNumber(clientes.getPageable().getPageNumber());
        response.setTotalPages(clientes.getTotalPages());
        response.setTotalElementes(clientes.getTotalElements());
        response.setNumberOfElements(clientes.getNumberOfElements());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .criar(clientes.getContent().get(0))).withRel("Criar novo Cliente: "));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ClienteDTO>> buscar(@PathVariable Long id) {
        Response<ClienteDTO> response = new Response<>();
        ClienteDTO cliente = clienteService.buscarPorId(id);

        if (cliente != null) {
            ClienteDTO clienteDTO = modelMapper.map(cliente, ClienteDTO.class);
            response.setData(clienteDTO);
            response.setStatusCode(HttpStatus.OK.value());

            response.add(Link.of("http://localhost:8080/clientes")
                    .withRel("Buscar todos os Clientes: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .atualizar(cliente.getId(), clienteDTO)).withRel("Atualizar Cliente: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .criar(clienteDTO)).withRel("Criar novo Cliente: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .deletar(cliente.getId())).withRel("Remover Clientes: "));
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Response<ClienteDTO>> criar(@RequestBody @Valid ClienteDTO clienteDTO) {
        Response<ClienteDTO> response = new Response<>();
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        ClienteDTO clienteDTO1 = modelMapper.map(clienteService.criar(cliente), ClienteDTO.class);
        response.setData(clienteDTO1);
        response.setStatusCode(HttpStatus.CREATED.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .buscar(clienteDTO1.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/clientes")
                .withRel("Buscar todos os Clientes: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .atualizar(clienteDTO1.getId(), clienteDTO1)).withRel("Atualizar Cliente: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .deletar(clienteDTO1.getId())).withRel("Remover Clientes: "));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ClienteDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDTO) {
        Response<ClienteDTO> response = new Response<>();
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente.setId(id);
        response.setData(modelMapper.map(clienteService.atualizar(cliente), ClienteDTO.class));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .buscar(clienteDTO.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/clientes")
                .withRel("Buscar todos os Clientes: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .criar(clienteDTO)).withRel("Criar novo Cliente: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                .deletar(cliente.getId())).withRel("Remover Clientes: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable Long id) {
        Response<String> response = new Response<>();
        response.setData(String.valueOf(clienteService.deletar(id)));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(Link.of("http://localhost:8080/clientes")
                .withRel("Buscar todos os Clientes: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Response<List<ClienteDTO>>> buscarPorEmail(@PathVariable String email) {
        Response<List<ClienteDTO>> response = new Response<>();
        List<ClienteDTO> clientes = clienteService.findByEmail(email).stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());

        response.setData(clientes);
        response.setStatusCode(HttpStatus.OK.value());

        if (!clientes.isEmpty()) {
            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .criar(clientes.get(0))).withRel("Criar novo Cliente: "));
        }

        return ResponseEntity.ok(response);
    }
}
