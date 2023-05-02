package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.api.controller.ClienteController;
import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.repository.ClienteRepository;
import com.mtz.vendasapi.domain.service.IClienteService;
import com.mtz.vendasapi.infrastructure.ClienteSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImp implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Page<ClienteDTO> listar(String filtro, String ordenacao, int pagina) {

        try {
            Page<ClienteDTO> clientesDTO = this.clienteRepository.findAll(ClienteSpecs.filtrarPor(filtro),
                    PageRequest.of(pagina, 10, Sort.by(ordenacao))).map(cliente -> new ClienteDTO(cliente));

            clientesDTO.forEach(cliente -> cliente.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class)
                    .buscar(cliente.getId())).withRel("Buscar Pelo ID: ")));

            return clientesDTO;
        } catch (Exception e) {
            throw new NegocioException("Erro interno. ");
        }
    }

    @Override
    public ClienteDTO buscarPorId(Long id) {
        try {
            Optional<Cliente> clienteOptional = this.clienteRepository.findById(id);
            if (clienteOptional.isPresent()) {
                return new ClienteDTO(clienteOptional.get());
            }
            throw new NegocioException("Cliente não foi localizado. ");
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException("Erro interno. ");
        }
    }

    @Override
    public ClienteDTO criar(Cliente cliente) {
        try {
            if (cliente.getId() != null) {
                throw new NegocioException("ID não pode ser informado na operação de cadastro.");
            }
            return this.cadastrarOuAtualizar(cliente);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException("Erro interno identificado. Contate o suporte.");
        }
    }

    @Override
    public ClienteDTO atualizar(Cliente cliente) {
        try {
            this.buscarPorId(cliente.getId());
            return this.cadastrarOuAtualizar(cliente);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException("Erro interno identificado. Contate o suporte.");
        }
    }

    @Override
    public ResponseEntity<String> deletar(Long id) {
        try {
            this.clienteRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente excluido com sucesso.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        } catch (DataIntegrityViolationException i) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Esse Cliente possui pedidos, não é possivel excluir.");
        }
    }

    private ClienteDTO cadastrarOuAtualizar(Cliente cliente) {
        Cliente clienteEntity = this.clienteRepository.save(cliente);
        ClienteDTO clienteDTO = new ClienteDTO(clienteEntity);
        return clienteDTO;
    }

}
