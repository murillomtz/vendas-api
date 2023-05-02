package com.mtz.vendasapi.domain.service;

import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import org.springframework.data.domain.Page;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;
import org.springframework.http.ResponseEntity;

public interface IClienteService {

	public Page<ClienteDTO> listar(String filtro, String ordenacao, int pagina);

	public ClienteDTO buscarPorId(Long id) throws NegocioException ;

	public ClienteDTO criar(Cliente cliente);

	public ClienteDTO atualizar(Cliente cliente) throws NegocioException;

	public ResponseEntity<String> deletar(Long id);
}