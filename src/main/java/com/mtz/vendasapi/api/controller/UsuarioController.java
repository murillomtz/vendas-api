package com.mtz.vendasapi.api.controller;

import com.mtz.vendasapi.api.model.dto.UsuarioDTO;
import com.mtz.vendasapi.config.SwaggerConfig;
import com.mtz.vendasapi.domain.model.Response;
import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.service.IUsuarioService;
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

@Api(tags = SwaggerConfig.USUARIO)
@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioController(IUsuarioService usuarioService, ModelMapper modelMapper) {
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Response<Page<UsuarioDTO>>> listar(@RequestParam(value = "filtro", required = false) String filtro,
                                                             @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
                                                             @RequestParam(value = "pagina", defaultValue = "0") int pagina) {

        Response<Page<UsuarioDTO>> response = new Response<>();
        Page<UsuarioDTO> usuarios = this.usuarioService.listar(filtro, ordenacao, pagina);

        //response.setData(usuarios);
        response.setContent(usuarios.getContent());
        response.setStatusCode(HttpStatus.OK.value());
        response.setSize(usuarios.getSize());
        response.setPageNumber(usuarios.getPageable().getPageNumber());
        response.setTotalPages(usuarios.getTotalPages());
        response.setTotalElementes(usuarios.getTotalElements());
        response.setNumberOfElements(usuarios.getNumberOfElements());

        if (!usuarios.getContent().isEmpty() && usuarios.getContent().get(0) != null) {
            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                    .criar(usuarios.getContent().get(0))).withRel("Criar novo Usuário: "));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UsuarioDTO>> buscar(@PathVariable Long id) {
        Response<UsuarioDTO> response = new Response<>();
        UsuarioDTO usuarioDTO = this.usuarioService.buscarPorId(id);
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        response.setData(usuarioDTO);
        response.setStatusCode(HttpStatus.OK.value());

        if (usuario != null) {

            response.add(Link.of("http://localhost:8080/usuarios")
                    .withRel("Buscar todos os Usuarios: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                    .atualizar(usuario.getId(), usuarioDTO)).withRel("Atualizar Usuario: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                    .criar(usuarioDTO)).withRel("Criar novo Usuario: "));

            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                    .deletar(usuario.getId())).withRel("Remover Usuario: "));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Response<UsuarioDTO>> criar(@RequestBody @Valid UsuarioDTO usuarioDTO) {

        Response<UsuarioDTO> response = new Response<>();
        UsuarioDTO usuarioDTO1 = this.usuarioService.criar(modelMapper.map(usuarioDTO, Usuario.class));
        response.setData(usuarioDTO1);
        response.setStatusCode(HttpStatus.CREATED.value());


        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .buscar(usuarioDTO.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/usuarios")
                .withRel("Buscar todos os Usuários: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .atualizar(usuarioDTO.getId(), usuarioDTO)).withRel("Atualizar Usuário: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .deletar(usuarioDTO.getId())).withRel("Remover Usuário: "));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UsuarioDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {


        Response<UsuarioDTO> response = new Response<>();
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuario.setId(id);
        response.setData(this.usuarioService.atualizar(usuario));
        response.setStatusCode(HttpStatus.OK.value());
        UsuarioDTO usuarioDTOChenger = modelMapper.map(usuario, UsuarioDTO.class);

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .buscar(usuarioDTOChenger.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/usuarios")
                .withRel("Buscar todos os Usuários: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .criar(usuarioDTOChenger)).withRel("Criar novo Usuário: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .deletar(usuario.getId())).withRel("Remover Usuário: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable Long id) {

        Response<String> response = new Response<>();
        response.setData(String.valueOf(this.usuarioService.deletar(id)));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(Link.of("http://localhost:8080/usuarios")
                .withRel("Buscar todos os Usuários: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Response<List<UsuarioDTO>>> buscarPorEmail(@PathVariable String email) {

        Response<List<UsuarioDTO>> response = new Response<>();
        List<UsuarioDTO> usuarios = this.usuarioService.findByEmail(email);

        response.setData(usuarios);

        response.setStatusCode(HttpStatus.OK.value());

        if (!usuarios.isEmpty()) {
            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                    .criar(usuarios.get(0))).withRel("Criar novo Usuário: "));
        }
        return ResponseEntity.ok(response);
    }

}
