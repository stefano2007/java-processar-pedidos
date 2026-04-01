package com.stefano.java_processar_pedidos.controller.dto;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import com.stefano.java_processar_pedidos.entity.PedidoItemEntity;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponse(
        Long pedidoId,
        BigDecimal valorTotal,
        List<ItemResponse> itens
) {
    public static PedidoResponse of(PedidoEntity pedido) {
        return new PedidoResponse(
                pedido.getPedidoId(),
                pedido.getValorTotal(),
                pedido.getItens().stream().map(ItemResponse::of).toList()
        );
    }

    public record ItemResponse(
            String produto,
            Integer quantidade,
            BigDecimal preco) {

        public static ItemResponse of(PedidoItemEntity item) {
            return new ItemResponse(item.getProduto(), item.getQuantidade(), item.getPreco());
        }
    }
}
