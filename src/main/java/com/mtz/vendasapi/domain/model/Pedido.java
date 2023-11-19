package com.mtz.vendasapi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_atendente")
    private Usuario atendente;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(name = "data_pedido")
    private Date dataPedido;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "pedidos_produtos", joinColumns = @JoinColumn(name = "id_pedido"), inverseJoinColumns = @JoinColumn(name = "id_produto"))
    private Set<Produto> produtos;

    public Pedido() {
        this.valorTotal = BigDecimal.ZERO;
    }
}
