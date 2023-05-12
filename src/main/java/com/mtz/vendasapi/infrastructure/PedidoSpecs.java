package com.mtz.vendasapi.infrastructure;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.mtz.vendasapi.domain.model.Pedido;

import java.math.BigDecimal;

public class PedidoSpecs {

    public static Specification<Pedido> filtrarPor(String filtro) {


        return (root, query, builder) -> {
            if (filtro == null) {
                return null;
            }
            Predicate predicate = builder.or(
                    //zbuilder.like(builder.lower(root.get("cliente")), "%" + filtro.toLowerCase() + "%"),
                    //builder.like(builder.lower(root.get("dataPedido")), "%" + filtro.toLowerCase() + "%"),
                    builder.equal(root.get("valorTotal"), new BigDecimal(filtro)));
            return predicate;
        };
    }
}
/**
 * predicates.add(builder.or(
 * builder.equal(root.get("cliente"), "%" + filtroMinusculo + "%")
 * //builder.like(builder.lower(root.get("dataPedido")), "%" + filtroMinusculo + "%"),
 * //                        builder.equal(root.get("valorTotal") , new BigDecimal(filtro) )
 * ));
 **/