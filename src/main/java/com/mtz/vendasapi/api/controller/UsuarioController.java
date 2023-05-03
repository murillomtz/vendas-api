package com.mtz.vendasapi.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Response;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.model.dto.UsuarioDTO;
import com.mtz.vendasapi.domain.service.IUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

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


        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .criar(usuarios.getContent().get(0))).withRel("Criar novo Usuário: "));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UsuarioDTO>> buscar(@PathVariable Long id) {
        Response<UsuarioDTO> response = new Response<>();
        UsuarioDTO usuarioDTO = this.usuarioService.buscarPorId(id);
        Usuario usuario = usuarioDTO.toEntity();
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
        UsuarioDTO usuarioDTO1 = this.usuarioService.criar(usuarioDTO.toEntity());
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
        Usuario usuario = usuarioDTO.toEntity();
        usuario.setId(id);
        response.setData(this.usuarioService.atualizar(usuario));
        response.setStatusCode(HttpStatus.OK.value());
        UsuarioDTO usuarioDTOChenger = new UsuarioDTO(usuario);

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .buscar(usuarioDTO.getId())).withRel("Buscar Pelo ID: "));

        response.add(Link.of("http://localhost:8080/usuarios")
                .withRel("Buscar todos os Usuários: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .criar(usuarioDTO)).withRel("Criar novo Usuário: "));

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .deletar(usuario.getId())).withRel("Remover Usuário: "));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deletar(@PathVariable Long id) {

        Response<String> response = new Response<>();
        response.setData(String.valueOf(this.usuarioService.deletar(id).getBody()));
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

        if (!usuarios.isEmpty() ) {
            response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)).
                    withRel("Criar novo Usuário: "));
        }
        return ResponseEntity.ok(response);
    }

}
