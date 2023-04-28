package com.mtz.vendasapi.api.controller;

import java.net.URI;

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

import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.service.IClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping
	public ResponseEntity<Page<ClienteDTO>> listar(@RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao,
			@RequestParam(value = "pagina", defaultValue = "0") int pagina) {

		Page<ClienteDTO> clientes = clienteService.listar(filtro, ordenacao, pagina)
				.map(cliente -> new ClienteDTO(cliente));

		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> buscar(@PathVariable Long id) {
		Cliente cliente = clienteService.buscarPorId(id);
		if (cliente != null) {
			return ResponseEntity.ok(new ClienteDTO(cliente));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<ClienteDTO> criar(@RequestBody @Valid ClienteDTO clienteDTO, HttpServletResponse response) {
		Cliente cliente = clienteService.criar(clienteDTO.toEntity());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(cliente.getId())
				.toUri();
		response.setHeader("Location", uri.toASCIIString());
		return ResponseEntity.created(uri).body(new ClienteDTO(cliente));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDTO) {
		Cliente cliente = clienteDTO.toEntity();
		cliente.setId(id);
		cliente = clienteService.atualizar(cliente);
		if (cliente != null) {
			return ResponseEntity.ok(new ClienteDTO(cliente));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletar(@PathVariable Long id) {

		return clienteService.deletar(id);
	}

}
