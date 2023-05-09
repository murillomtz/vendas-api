package com.mtz.vendasapi.domain.repository;

import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.model.dto.ClienteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mtz.vendasapi.domain.model.Cliente;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {

    List<Cliente> findByEmail(String email);

}
