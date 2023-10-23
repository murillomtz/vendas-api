package com.mtz.vendasapi.domain.service.serviceImp;

import com.mtz.vendasapi.api.controller.UsuarioController;
import com.mtz.vendasapi.api.model.dto.UsuarioDTO;
import com.mtz.vendasapi.domain.constant.MensagensConstant;
import com.mtz.vendasapi.domain.exception.NegocioException;
import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.repository.UsuarioRepository;
import com.mtz.vendasapi.domain.service.IUsuarioService;
import com.mtz.vendasapi.infrastructure.UsuarioSpecs;
import org.modelmapper.ModelMapper;
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
public class UsuarioServiceImp implements IUsuarioService {


    private final UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioServiceImp(UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<UsuarioDTO> listar(String filtro, String ordenacao, int pagina) {

        try {
            Page<UsuarioDTO> usuarioDTO = this.usuarioRepository.findAll(UsuarioSpecs.
                            filtrarPor(filtro), PageRequest.of(pagina, 10, Sort.by(ordenacao))).
                    map(usuario -> modelMapper.map(usuario, UsuarioDTO.class));
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
                return modelMapper.map(usuarioOptional.get(), UsuarioDTO.class);
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
    public String deletar(Long id) {

        try {
            this.usuarioRepository.deleteById(id);
            return MensagensConstant.OK_EXCLUIDO_COM_SUCESSO.getValor();
        } catch (EmptyResultDataAccessException e) {
            return MensagensConstant.ERRO_USUARIO_NAO_ENCONTRADO.getValor();
        } catch (DataIntegrityViolationException i) {
            return MensagensConstant.ERRO_EXCLUIR_USUARIO.getValor();
        }
    }

    public List<UsuarioDTO> findByEmail(String email) {

        try {
            List<Usuario> usuarios = usuarioRepository.findByEmail(email);

            if (usuarios.isEmpty()) {
                throw new NegocioException(MensagensConstant.ERRO_USUARIO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
            }
            List<UsuarioDTO> usuariosDTO = new ArrayList<>();
            usuarios.forEach(usuario -> usuariosDTO.add(modelMapper.map(usuario, UsuarioDTO.class)));

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
        UsuarioDTO usuarioDTO = modelMapper.map(usuarioEntity, UsuarioDTO.class);
        return usuarioDTO;
    }
}