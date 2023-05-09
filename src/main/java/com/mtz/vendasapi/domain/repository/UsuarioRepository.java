package com.mtz.vendasapi.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mtz.vendasapi.domain.model.Usuario;

@Repository
public interface UsuarioRepository
		extends PagingAndSortingRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	List<Usuario> findByEmail(String email);

}
