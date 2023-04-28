package com.mtz.vendasapi.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	public ResponseEntity<Page<UsuarioDTO>> listar(@RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
			@RequestParam(value = "pagina", defaultValue = "0") int pagina) {

		Page<UsuarioDTO> usuarios = usuarioService.listar(filtro, ordenacao, pagina)
				.map(usuario -> new UsuarioDTO(usuario));

		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> buscar(@PathVariable Long id) {
		Usuario usuario = usuarioService.buscarPorId(id);
		if (usuario != null) {
			return ResponseEntity.ok(new UsuarioDTO(usuario));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<UsuarioDTO> criar(@RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response) {
		Usuario usuario = usuarioService.criar(usuarioDTO.toEntity());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(usuario.getId())
				.toUri();
		response.setHeader("Location", uri.toASCIIString());
		return ResponseEntity.created(uri).body(new UsuarioDTO(usuario));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioDTO.toEntity();
		usuario.setId(id);
		usuario = usuarioService.atualizar(usuario);
		if (usuario != null) {
			return ResponseEntity.ok(new UsuarioDTO(usuario));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletar(@PathVariable Long id) {
		
		return usuarioService.deletar(id);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<List<Optional<Usuario>>> buscarPorEmail(@PathVariable String email) {
		List<Optional<Usuario>> usuarios = usuarioService.findByEmail(email);

		if (usuarios != null) {
			return ResponseEntity.ok(usuarios);
		}
		return ResponseEntity.notFound().build();
	}

}
