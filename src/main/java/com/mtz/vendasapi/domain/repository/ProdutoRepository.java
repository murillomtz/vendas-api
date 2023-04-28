package com.mtz.vendasapi.domain.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mtz.vendasapi.domain.model.Produto;

@Repository
public interface ProdutoRepository extends PagingAndSortingRepository<Produto, Long> , JpaSpecificationExecutor<Produto> {

}

