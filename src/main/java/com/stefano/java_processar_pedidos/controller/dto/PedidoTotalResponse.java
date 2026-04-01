package com.stefano.java_processar_pedidos.controller.dto;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;

import java.math.BigDecimal;

public record PedidoTotalResponse(
        Long pedidoId,
        BigDecimal valorTotal) {

    public static PedidoTotalResponse of(PedidoEntity pedido) {
        return new PedidoTotalResponse(pedido.getPedidoId(), pedido.getValorTotal());
    }
}
