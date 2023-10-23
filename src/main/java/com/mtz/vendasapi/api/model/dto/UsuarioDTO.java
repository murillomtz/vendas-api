package com.mtz.vendasapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {


    private Long id;

    @NotBlank(message = "O nome do usuário não pode estar em branco")
    @Size(max = 50, message = "O nome do usuário não pode ter mais de 50 caracteres")
    private String nome;

    @NotBlank(message = "O email do usuário não pode estar em branco")
    @Size(max = 100, message = "O email do usuário não pode ter mais de 100 caracteres")
    private String email;

    @NotBlank(message = "A senha do usuário não pode estar em branco")
    @Size(max = 100, message = "A senha do usuário não pode ter mais de 100 caracteres")
    private String senha;

    @NotNull(message = "A data de criação do usuário não pode ser nula")
    private Date dataCriacao;

    public UsuarioDTO() {
        // construtor padrão sem argumentos
    }


}
