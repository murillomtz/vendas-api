package com.mtz.vendasapi.infrastructure;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.mtz.vendasapi.domain.model.Cliente;

public class ClienteSpecs {

	public static Specification<Cliente> filtrarPor(String filtro) {
		return (root, query, builder) -> {

			var predicates = new ArrayList<Predicate>();

			if (filtro != null && !filtro.isEmpty()) {
				var filtroMinusculo = filtro.toLowerCase();
				predicates.add(builder.or(builder.like(builder.lower(root.get("nome")), "%" + filtroMinusculo + "%"),
						builder.like(builder.lower(root.get("sobrenome")), "%" + filtroMinusculo + "%"),
						builder.like(builder.lower(root.get("endereco")), "%" + filtroMinusculo + "%"),
						builder.like(builder.lower(root.get("cidade")), "%" + filtroMinusculo + "%"),
						builder.like(builder.lower(root.get("estado")), "%" + filtroMinusculo + "%"),
						builder.like(builder.lower(root.get("telefone")), "%" + filtroMinusculo + "%")));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
