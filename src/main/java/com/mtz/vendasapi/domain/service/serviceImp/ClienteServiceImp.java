package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.api.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.repository.ClienteRepository;
import com.mtz.vendasapi.domain.service.IClienteService;
import com.mtz.vendasapi.infrastructure.ClienteSpecs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImp implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClienteServiceImp(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ClienteDTO> listar(String filtro, String ordenacao, int pagina) {
        try {
            Page<Cliente> clientes = clienteRepository.findAll(
                    ClienteSpecs.filtrarPor(filtro),
                    PageRequest.of(pagina, 10, Sort.by(ordenacao))
            );
            return clientes.map(cliente -> modelMapper.map(cliente, ClienteDTO.class));
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ClienteDTO buscarPorId(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            return modelMapper.map(clienteOptional.get(), ClienteDTO.class);
        }
        throw new NegocioException(MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
    }

    @Override
    public ClienteDTO criar(Cliente cliente) {
        if (cliente.getId() != null) {
            throw new NegocioException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
        }
        return cadastrarOuAtualizar(cliente);
    }

    @Override
    public ClienteDTO atualizar(Cliente cliente) {
        buscarPorId(cliente.getId());
        return cadastrarOuAtualizar(cliente);
    }

    @Override
    public String deletar(Long id) {
        try {
            clienteRepository.deleteById(id);
            return MensagensConstant.OK_EXCLUIDO_COM_SUCESSO.getValor();
        } catch (EmptyResultDataAccessException e) {
            return MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor();
        } catch (DataIntegrityViolationException i) {
            return MensagensConstant.ERRO_EXCLUIR_CLIENTE.getValor();
        }
    }

    @Override
    public List<ClienteDTO> findByEmail(String email) {
        List<Cliente> clientes = clienteRepository.findByEmail(email);
        if (clientes.isEmpty()) {
            throw new NegocioException(MensagensConstant.ERRO_CLIENTE_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        }
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    private ClienteDTO cadastrarOuAtualizar(Cliente cliente) {
        Cliente clienteEntity = clienteRepository.save(cliente);
        return modelMapper.map(clienteEntity, ClienteDTO.class);
    }
}
