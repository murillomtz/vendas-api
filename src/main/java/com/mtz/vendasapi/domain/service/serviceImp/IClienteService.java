package com.mtz.vendasapi.domain.service.serviceImp;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;

public interface IClienteService {

	public Page<Cliente> listar(String filtro, String ordenacao, int pagina);

	public Cliente buscarPorId(Long id) throws NegocioException ;

	public Cliente criar(Cliente cliente);

	public Cliente atualizar(Cliente cliente) throws NegocioException;

	public ResponseEntity<String> deletar(Long id);
}