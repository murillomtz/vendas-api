package com.mtz.vendasapi.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Usuario;

public interface IUsuarioService {

	public Page<Usuario> listar(String filtro, String ordenacao, int pagina);

	public Usuario buscarPorId(Long id) throws NegocioException;

	public Usuario criar(Usuario usuario);

	public Usuario atualizar(Usuario usuario) throws NegocioException;

	public ResponseEntity<String> deletar(Long id);

	public List<Optional<Usuario>> findByEmail(String email);
}