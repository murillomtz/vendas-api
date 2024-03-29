package com.mtz.vendasapi.domain.service;

import java.util.List;

import com.mtz.vendasapi.api.model.dto.UsuarioDTO;
import org.springframework.data.domain.Page;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Usuario;

public interface IUsuarioService {

	public Page<UsuarioDTO>  listar(String filtro, String ordenacao, int pagina);

	public UsuarioDTO buscarPorId(Long id) throws NegocioException;

	public UsuarioDTO criar(Usuario usuario);

	public UsuarioDTO atualizar(Usuario usuario) throws NegocioException;

	public String deletar(Long id);

	public List<UsuarioDTO> findByEmail(String email);
}