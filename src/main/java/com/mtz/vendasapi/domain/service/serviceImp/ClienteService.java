package com.mtz.vendasapi.domain.service.serviceImp;

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
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.repository.ClienteRepository;
import com.mtz.vendasapi.infrastructure.ClienteSpecs;

@Service
public class ClienteService implements IClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

//	@Autowired
//	private PedidoRepository pedidoRepository;

	public Page<Cliente> listar(String filtro, String ordenacao, int pagina) {
		return clienteRepository.findAll(ClienteSpecs.filtrarPor(filtro),
				PageRequest.of(pagina, 10, Sort.by(ordenacao)));
	}

	public Cliente buscarPorId(Long id) throws NegocioException {
		Optional<Cliente> clienteOptional = clienteRepository.findById(id);
		if (clienteOptional.isPresent()) {
			return clienteOptional.get();
		} else {
			throw new NegocioException("Cliente não encontrado");
		}
	}

	public Cliente criar(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	public Cliente atualizar(Cliente cliente) throws NegocioException {
		Cliente clienteExistente = buscarPorId(cliente.getId());
		if (clienteExistente != null) {
			return clienteRepository.save(cliente);
		} else {
			throw new NegocioException("Cliente não encontrado");
		}
	}

	public ResponseEntity<String> deletar(Long id) {
		try {
			clienteRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Cliente excluido com sucesso.");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		} catch (DataIntegrityViolationException i) {
//			throw new NegocioException("Esse Cliente possui pedidos, não é possivel excluir.");
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Esse Cliente possui pedidos, não é possivel excluir.");
		}
	}
}
