package com.stefano.java_processar_pedidos.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ClienteResumoPedidoResponse(
        @JsonProperty("clienteId") Long id,
        @JsonProperty("totalPedidos") BigDecimal totalPedidos,
        @JsonProperty("quantidadePedidos") Integer quantidadePedidos
) {
}
