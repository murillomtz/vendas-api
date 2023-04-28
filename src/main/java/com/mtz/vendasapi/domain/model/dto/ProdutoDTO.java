package com.mtz.vendasapi.domain.model.dto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mtz.vendasapi.domain.model.Produto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProdutoDTO {

//	@NotNull(message = "O ID do produto não pode ser nulo")
	private Long id;

	@NotBlank(message = "O nome do produto não pode estar em branco")
	@Size(max = 50, message = "O nome do produto não pode ter mais de 100 caracteres")
	private String nome;

	@Size(max = 300, message = "O nome do produto não pode ter mais de 100 caracteres")
	@Column(name = "descricao")
	private String descricao;

	@NotNull(message = "O preço do produto não pode ser nulo")
	@DecimalMin(value = "0.01", message = "O preço do produto deve ser maior que zero")
	private BigDecimal valorVenda;

	@NotNull(message = "A quantidade do produto não pode ser nula")
	@Min(value = 1, message = "A quantidade do produto deve ser pelo menos 1")
	@Max(value = 1000, message = "A quantidade do produto não pode ser maior que 1000")
	private Integer quantidade;

	public ProdutoDTO(Produto produto) {
		this.id = produto.getId();
		this.nome = produto.getNome();
		this.descricao = produto.getDescricao();
		this.valorVenda = produto.getValorVenda();
		this.quantidade = produto.getQuantidade();
	}

	public Produto toEntity() {
		Produto produto = new Produto();
		produto.setId(this.id);
		produto.setNome(this.nome);
		produto.setDescricao(this.descricao);
		produto.setValorVenda(this.valorVenda);
		produto.setQuantidade(this.quantidade);
		return produto;
	}

}
