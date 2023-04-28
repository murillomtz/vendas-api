package com.mtz.vendasapi.infrastructure;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.mtz.vendasapi.domain.model.Pedido;

public class PedidoSpecs {

	public static Specification<Pedido> filtrarPor(String filtro) {
		
		
		return (root, query, builder) -> {
			if (filtro == null) {
				return null;
			}
			Predicate predicate = builder.or(
					//builder.like(builder.lower(root.get("cliente")), "%" + filtro.toLowerCase() + "%"),
					//builder.like(builder.lower(root.get("dataPedido")), "%" + filtro.toLowerCase() + "%"),
					builder.equal(builder.lower(root.get("valorTotal")), "%" + filtro + "%"));
			return predicate;
		};
	}
}
