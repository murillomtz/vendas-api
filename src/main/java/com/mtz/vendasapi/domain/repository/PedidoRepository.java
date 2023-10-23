package com.mtz.vendasapi.domain.repository;

import com.mtz.vendasapi.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.produtos prod WHERE prod.id = :idProduto")
    List<Pedido> findByProdutoId(@Param("idProduto") Long idProduto);

    //	@Query("DELETE FROM Pedido WHERE id_cliente = :id_cliente")
    //public void deleteByCliente(@Param("id_cliente") Long id);

    @Query("SELECT DISTINCT p FROM Pedido p WHERE p.cliente.id = :idCliente")
    List<Pedido> findByClienteId(@Param("idCliente") Long idCliente);


//	@Query("DELETE FROM pedidos_produtos WHERE id_pedido = :id_pedido")sOs
//	public void deleteByPedido(@Param("id_pedido") Long id);

}
