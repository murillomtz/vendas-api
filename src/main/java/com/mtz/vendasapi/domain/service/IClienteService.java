package com.mtz.vendasapi.domain.service;

import com.mtz.vendasapi.api.model.dto.ClienteDTO;
import org.springframework.data.domain.Page;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;

import java.util.List;

public interface IClienteService {

	public Page<ClienteDTO> listar(String filtro, String ordenacao, int pagina);

	public ClienteDTO buscarPorId(Long id) throws NegocioException ;

	public ClienteDTO criar(Cliente cliente);

	public ClienteDTO atualizar(Cliente cliente) throws NegocioException;

	public String deletar(Long id);

	public List<ClienteDTO> findByEmail(String email);
}