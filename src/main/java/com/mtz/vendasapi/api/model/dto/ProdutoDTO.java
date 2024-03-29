package com.mtz.vendasapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class ProdutoDTO extends RepresentationModel<ProdutoDTO> {

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

    public ProdutoDTO() {
        // construtor padrão sem argumentos
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProdutoDTO produto = (ProdutoDTO) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
