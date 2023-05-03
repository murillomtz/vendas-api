package com.mtz.vendasapi.domain.service.serviceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mtz.vendasapi.api.controller.ClienteController;
import com.mtz.vendasapi.api.controller.UsuarioController;
import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import com.mtz.vendasapi.domain.model.dto.UsuarioDTO;
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
    public Page<UsuarioDTO> listar(String filtro, String ordenacao, int pagina) {

        try {
            Page<UsuarioDTO> usuarioDTO = this.usuarioRepository.findAll(UsuarioSpecs.
                            filtrarPor(filtro), PageRequest.of(pagina, 10, Sort.by(ordenacao))).
                    map(usuario -> new UsuarioDTO(usuario));

            usuarioDTO.forEach(usuario -> usuario.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).
                    buscar(usuario.getId())).withRel("Buscar Pelo ID: ")));

            return usuarioDTO;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {

        try {
            Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(id);
            if (usuarioOptional.isPresent()) {
                return new UsuarioDTO(usuarioOptional.get());
            }
            throw new NegocioException(MensagensConstant.ERRO_USUARIO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UsuarioDTO criar(Usuario usuario) {
        try {
            if (usuario.getId() != null) {
                throw new NegocioException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }
            return this.cadastrarOuAtualizar(usuario);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UsuarioDTO atualizar(Usuario usuario) throws NegocioException {

        try {
            this.buscarPorId(usuario.getId());
            return this.cadastrarOuAtualizar(usuario);
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deletar(Long id) {
        try {
            this.usuarioRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(MensagensConstant.OK_EXCLUIDO_COM_SUCESSO.getValor());
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MensagensConstant.ERRO_USUARIO_NAO_ENCONTRADO.getValor());
        } catch (DataIntegrityViolationException i) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(MensagensConstant.ERRO_EXCLUIR_USUARIO.getValor());
        }
    }

    public List<UsuarioDTO> findByEmail(String email) {

        try {
            List<Usuario> usuarios = usuarioRepository.findByEmail(email);

            if (usuarios.isEmpty()) {
                throw new NegocioException(MensagensConstant.ERRO_USUARIO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
            }
            List<UsuarioDTO> usuariosDTO = new ArrayList<>();
            usuarios.forEach(usuario -> usuariosDTO.add(new UsuarioDTO(usuario)));

            usuariosDTO.forEach(usuario -> usuario.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).
                    buscar(usuario.getId())).withRel("Buscar Pelo ID: ")));

            return usuariosDTO;
        } catch (NegocioException m) {
            throw m;
        } catch (Exception e) {
            throw new NegocioException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UsuarioDTO cadastrarOuAtualizar(Usuario usuario) {
        Usuario usuarioEntity = this.usuarioRepository.save(usuario);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioEntity);
        return usuarioDTO;
    }
}