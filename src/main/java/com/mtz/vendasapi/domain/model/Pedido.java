package com.mtz.vendasapi.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pedidos")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_atendente")
	private Usuario atendente;

	@ManyToOne(cascade = CascadeType.MERGE)
	// @OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@Column(name = "data_pedido")
	private Date dataPedido;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "pedidos_produtos", joinColumns = @JoinColumn(name = "id_pedido"), inverseJoinColumns = @JoinColumn(name = "id_produto"))
	private Set<Produto> produtos;

}
