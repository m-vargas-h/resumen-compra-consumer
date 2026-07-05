package com.duoc.resumen_compra_consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.resumen_compra_consumer.model.ResumenCompra;

@Repository
public interface ResumenCompraRepository extends JpaRepository<ResumenCompra, Long> {
}