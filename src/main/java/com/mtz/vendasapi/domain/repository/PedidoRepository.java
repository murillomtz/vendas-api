package com.mtz.vendasapi.domain.repository;

import java.util.List;

import com.mtz.vendasapi.domain.model.dto.PedidoDTO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mtz.vendasapi.domain.model.Pedido;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    @Query("SELECT p FROM Pedido p JOIN p.produtos prod WHERE prod.id = :idProduto")
    List<PedidoDTO> findByProdutoId(@Param("idProduto") Long idProduto);

//	@Query("DELETE FROM Pedido WHERE id_cliente = :id_cliente")
//	public void deleteByCliente(@Param("id_cliente") Long id);

//	@Query("SELECT p FROM Pedido p WHERE p.id_cliente = :id_cliente")
//	List<Pedido> findByClienteId(@Param("id_cliente") Long id);

//	@Query("DELETE FROM pedidos_produtos WHERE id_pedido = :id_pedido")sOs
//	public void deleteByPedido(@Param("id_pedido") Long id);

}
