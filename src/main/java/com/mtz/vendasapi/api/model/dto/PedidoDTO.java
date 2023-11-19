package com.mtz.vendasapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class PedidoDTO extends RepresentationModel<PedidoDTO> {

    private Long id;

    @NotNull(message = "O Id do cliente é obrigatório.")
    private Long idCliente;

    @NotNull(message = "O Id do atendente é obrigatório.")
    private Long idAtendente;

    @NotNull(message = "A data do pedido é obrigatória.")
    private Date dataPedido;

    @NotNull(message = "O valor total é obrigatório.")
    @Positive(message = "O valor total deve ser maior que zero.")
    private BigDecimal valorTotal;

    private Set<ProdutoDTO> produtos;

    public PedidoDTO() {
        //Construtor vazio
    }

    public PedidoDTO(Long idAtendente, Long idCliente, Date dataPedido, BigDecimal valorTotal) {
        this.idCliente = idCliente;
        this.idAtendente = idAtendente;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
    }
}
