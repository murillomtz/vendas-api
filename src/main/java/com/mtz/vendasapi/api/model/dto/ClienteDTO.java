package com.mtz.vendasapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ClienteDTO extends RepresentationModel<ClienteDTO> {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O sobrenome é obrigatório")
    @Size(min = 2, max = 100, message = "O sobrenome deve conter entre 2 e 100 caracteres")
    private String sobrenome;

    @NotBlank(message = "O sexo é obrigatório")
    @Size(min = 1, max = 10, message = "O sexo deve conter entre 1 e 10 caracteres")
    private String sexo;

    @NotNull(message = "A data de nascimento é obrigatória")
    @PastOrPresent(message = "A data de nascimento não pode ser no futuro")
    private Date dataNascimento;

    @NotBlank(message = "A nacionalidade é obrigatória")
    @Size(min = 2, max = 50, message = "A nacionalidade deve conter entre 2 e 50 caracteres")
    private String nacionalidade;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(min = 5, max = 200, message = "O endereço deve conter entre 5 e 200 caracteres")
    private String endereco;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(min = 2, max = 100, message = "A cidade deve conter entre 2 e 100 caracteres")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 50, message = "O estado deve conter entre 2 e 50 caracteres")
    private String estado;

    @NotBlank(message = "O telefone é obrigatório")
    @Size(min = 8, max = 15, message = "O telefone deve conter entre 8 e 15 caracteres")
    private String telefone;

    @NotBlank(message = "O email do usuário não pode estar em branco")
    @Size(max = 100, message = "O email do usuário não pode ter mais de 100 caracteres")
    private String email;

    public ClienteDTO() {
        // Construtor vazio
    }
}
