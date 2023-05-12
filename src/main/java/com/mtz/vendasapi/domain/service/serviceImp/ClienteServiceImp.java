package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.api.controller.ClienteController;
import com.mtz.vendasapi.api.controller.UsuarioController;
import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.model.dto.UsuarioDTO;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImp implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Page<ClienteDTO> listar(String filtro, String ordenacao, int pagina) {

        try {
            Page<ClienteDTO> clientesDTO = this.clienteRepository.findAll(ClienteSpecs.
                            filtrarPor(filtro), PageRequest.of(pagina, 10, Sort.by(ordenacao))).
                    map(cliente -> new ClienteDTO(cliente));

            clientesDTO.forEach(cliente -> cliente.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).
                    buscar(cliente.getId())).withRel("Buscar Pelo ID: ")));

            return clientesDTO;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClienteDTO buscarPorId(Long id) {
        try {
            Optional<Cliente> clienteOptional = this.clienteRepository.findById(id);
            if (clienteOptional.isPresent()) {
                return new ClienteDTO(clienteOptional.get());
            }
            throw new NegocioException(MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClienteDTO criar(Cliente cliente) {
        try {
            if (cliente.getId() != null) {
                throw new NegocioException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }
            return this.cadastrarOuAtualizar(cliente);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
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
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deletar(Long id) {
        try {
            this.clienteRepository.deleteById(id);
            return MensagensConstant.OK_EXCLUIDO_COM_SUCESSO.getValor();
        } catch (EmptyResultDataAccessException e) {
            return MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor();
        } catch (DataIntegrityViolationException i) {
            return MensagensConstant.ERRO_EXCLUIR_CLIENTE.getValor();
        }
    }

    @Override
    public List<ClienteDTO> findByEmail(String email) {

        try {
            List<Cliente> clientes = this.clienteRepository.findByEmail(email);

            if (clientes.isEmpty()) {
                throw new NegocioException(MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
            }
            List<ClienteDTO> clienteDTO = new ArrayList<>();
            clientes.forEach(cliente -> clienteDTO.add(new ClienteDTO(cliente)));

            clienteDTO.forEach(usuario -> usuario.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).
                    buscar(usuario.getId())).withRel("Buscar Pelo ID: ")));

            return clienteDTO;
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClienteDTO cadastrarOuAtualizar(Cliente cliente) {
        Cliente clienteEntity = this.clienteRepository.save(cliente);
        ClienteDTO clienteDTO = new ClienteDTO(clienteEntity);
        return clienteDTO;
    }

}
