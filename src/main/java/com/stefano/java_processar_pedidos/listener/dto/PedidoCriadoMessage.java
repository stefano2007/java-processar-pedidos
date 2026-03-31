package com.stefano.java_processar_pedidos.listener.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoCriadoMessage(
        Long codigoPedido,
        Long codigoCliente,
        List<PedidoCriadoMessageItem> itens) {

    public record PedidoCriadoMessageItem(
            String produto,
            Integer quantidade,
            BigDecimal preco) {
    }
}
