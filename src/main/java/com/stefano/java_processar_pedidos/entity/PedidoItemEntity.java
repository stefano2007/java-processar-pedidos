package com.stefano.java_processar_pedidos.entity;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

public class PedidoItemEntity {

    private PedidoItemEntity() {
    }

    public PedidoItemEntity(String produto, Integer quantidade, BigDecimal preco) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    private String produto;

    private Integer quantidade;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal preco;

    public String getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
