package com.stefano.java_processar_pedidos.repository;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<PedidoEntity, Long> {
}
