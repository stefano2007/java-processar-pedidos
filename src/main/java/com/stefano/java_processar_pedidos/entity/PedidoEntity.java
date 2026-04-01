package com.stefano.java_processar_pedidos.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "tb_pedidos")
public class PedidoEntity {

    private PedidoEntity() {
    }

    public PedidoEntity(Long pedidoId, Long clienteId, List<PedidoItemEntity> itens) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.valorTotal = itens.stream()
                .map(i -> i.getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.itens = itens;
    }

    @MongoId
    private Long pedidoId;

    @Indexed(name = "idx_cliente_id")
    private Long clienteId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal valorTotal;

    private List<PedidoItemEntity> itens;

    public Long getPedidoId() {
        return pedidoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public List<PedidoItemEntity> getItens() {
        return itens;
    }
}
