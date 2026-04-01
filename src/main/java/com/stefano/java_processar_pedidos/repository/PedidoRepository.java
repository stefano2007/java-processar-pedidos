package com.stefano.java_processar_pedidos.repository;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<PedidoEntity, Long> {
    Page<PedidoEntity> findAllByClienteId(Long clienteId, Pageable pageable);

    Integer countByClienteId(Long clienteId);
}
