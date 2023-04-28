package com.mtz.vendasapi.infrastructure;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.mtz.vendasapi.domain.model.Usuario;

public class UsuarioSpecs {

    public static Specification<Usuario> filtrarPor(String filtro) {
        return (root, query, builder) -> {
            if (filtro == null) {
                return null;
            }
            Predicate predicate = builder.or(
                    builder.like(builder.lower(root.get("nome")), "%" + filtro.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get("email")), "%" + filtro.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get("dataCriacao")), "%" + filtro.toLowerCase() + "%"));
            return predicate;
        };
    }
}