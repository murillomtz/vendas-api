package com.mtz.vendasapi.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "produtos")
@AllArgsConstructor
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "valor_venda")
	private BigDecimal valorVenda;

	@Column(name = "quantidade")
	private Integer quantidade;

	public Produto() {

	}

	public Produto(String nome, BigDecimal valorVenda) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.valorVenda = valorVenda;
		this.quantidade = quantidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Produto produto = (Produto) o;
		return Objects.equals(id, produto.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}