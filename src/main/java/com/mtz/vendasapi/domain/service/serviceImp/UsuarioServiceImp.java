package com.mtz.vendasapi.domain.service.serviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.repository.UsuarioRepository;
import com.mtz.vendasapi.domain.service.IUsuarioService;
import com.mtz.vendasapi.infrastructure.UsuarioSpecs;

@Service
public class UsuarioServiceImp implements IUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Page<Usuario> listar(String filtro, String ordenacao, int pagina) {
		return usuarioRepository.findAll(UsuarioSpecs.filtrarPor(filtro),
				PageRequest.of(pagina, 10, Sort.by(ordenacao)));
	}

	@Override
	public Usuario buscarPorId(Long id) throws NegocioException {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
		if (usuarioOptional.isPresent()) {
			return usuarioOptional.get();
		} else {
			throw new NegocioException("Usuário não encontrado");
		}
	}

	@Override
	public Usuario criar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario atualizar(Usuario usuario) throws NegocioException {
		Usuario usuarioExistente = buscarPorId(usuario.getId());
		if (usuarioExistente != null) {

			usuarioExistente.setNome(usuario.getNome());
			usuarioExistente.setEmail(usuario.getEmail());
			usuarioExistente.setSenha(usuario.getSenha());
			usuarioExistente.setDataCriacao(usuario.getDataCriacao());
			return usuarioRepository.save(usuarioExistente);

		} else {
			throw new NegocioException("Usuário não encontrado");
		}
	}

	@Override
	public ResponseEntity<String>  deletar(Long id) {
		try {
			usuarioRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Usuário excluido com sucesso.");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		} catch (DataIntegrityViolationException i) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Esse Usuário possui pedidos, não é possivel excluir.");
		}
	}

	public List<Optional<Usuario>> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
}