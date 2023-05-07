package com.mtz.vendasapi.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nome")
	private String nome;

	@Column(name = "sobrenome")
	private String sobrenome;

	@Column(name = "email")
	private String email;

	@Column(name = "sexo")
	private String sexo;

	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@Column(name = "nacionalidade")
	private String nacionalidade;

	@Column(name = "endereco")
	private String endereco;

	@Column(name = "cidade")
	private String cidade;

	@Column(name = "estado")
	private String estado;

	@Column(name = "telefone")
	private String telefone;

}