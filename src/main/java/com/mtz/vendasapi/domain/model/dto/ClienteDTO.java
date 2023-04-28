package com.mtz.vendasapi.domain.model.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.mtz.vendasapi.domain.model.Cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClienteDTO {

	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
	private String nome;

	@NotBlank(message = "O sobrenome é obrigatório")
	@Size(min = 2, max = 100, message = "O sobrenome deve conter entre 2 e 100 caracteres")
	private String sobrenome;

	@NotBlank(message = "O sexo é obrigatório")
	private String sexo;

	@NotNull(message = "A data de nascimento é obrigatória")
	@PastOrPresent(message = "A data de nascimento não pode ser no futuro")
	private Date dataNascimento;

	@NotBlank(message = "A nacionalidade é obrigatória")
	private String nacionalidade;

	@NotBlank(message = "O endereço é obrigatório")
	private String endereco;

	@NotBlank(message = "A cidade é obrigatória")
	private String cidade;

	@NotBlank(message = "O estado é obrigatório")
	private String estado;

	@NotBlank(message = "O telefone é obrigatório")
	private String telefone;

	public ClienteDTO(Cliente cliente) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.sobrenome = cliente.getSobrenome();
		this.sexo = cliente.getSexo();
		this.dataNascimento = cliente.getDataNascimento();
		this.nacionalidade = cliente.getNacionalidade();
		this.endereco = cliente.getEndereco();
		this.cidade = cliente.getCidade();
		this.estado = cliente.getEstado();
		this.telefone = cliente.getTelefone();
	}

	public Cliente toEntity() {
		Cliente cliente = new Cliente();
		cliente.setId(id);
		cliente.setNome(nome);
		cliente.setSobrenome(sobrenome);
		cliente.setSexo(sexo);
		cliente.setDataNascimento(dataNascimento);
		cliente.setNacionalidade(nacionalidade);
		cliente.setEndereco(endereco);
		cliente.setCidade(cidade);
		cliente.setEstado(estado);
		cliente.setTelefone(telefone);
		return cliente;
	}

}
