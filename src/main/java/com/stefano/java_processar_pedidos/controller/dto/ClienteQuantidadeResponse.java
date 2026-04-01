package com.stefano.java_processar_pedidos.controller.dto;

public record ClienteQuantidadeResponse(
        Long clienteId,
        Integer quantidadePedidos) {
}
