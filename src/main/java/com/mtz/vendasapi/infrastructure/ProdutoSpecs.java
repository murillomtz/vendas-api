package com.mtz.vendasapi.infrastructure;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.mtz.vendasapi.domain.model.Produto;

public class ProdutoSpecs {
	//nome= &desc=

	public static Specification<Produto> filtrarPor(String filtro) {
		return (root, query, builder) -> {
			if (filtro == null) {
				return null;
			}
			Predicate nome = builder.like(builder.lower(root.get("nome")), "%" + filtro.toLowerCase() + "%");
			Predicate descricao = builder.like(builder.lower(root.get("descricao")), "%" + filtro.toLowerCase() + "%");
			return builder.or(nome, descricao);
		};
	}
}
