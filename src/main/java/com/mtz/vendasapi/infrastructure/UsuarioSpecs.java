package com.mtz.vendasapi.infrastructure;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.mtz.vendasapi.domain.model.Usuario;

import java.util.ArrayList;

public class UsuarioSpecs {

    public static Specification<Usuario> filtrarPor(String filtro) {
        return (root, query, builder) -> {

            var predicates = new ArrayList<Predicate>();


            if (filtro != null && !filtro.isEmpty()) {
                var filtroMinusculo = filtro.toLowerCase();

                predicates.add(builder.or(builder.like(
                        builder.lower(root.get("nome")), "%" + filtroMinusculo + "%"),
                        builder.like(builder.lower(root.get("email")), "%" + filtroMinusculo + "%")));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}