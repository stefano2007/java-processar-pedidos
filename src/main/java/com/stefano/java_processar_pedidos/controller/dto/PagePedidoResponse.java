package com.stefano.java_processar_pedidos.controller.dto;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagePedidoResponse(
        List<PedidoResponse> content,
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages
) {
    public static PagePedidoResponse of(Page<PedidoEntity> pagePedidos) {
        return new PagePedidoResponse(
                pagePedidos.getContent().stream().map(PedidoResponse::of).toList(),
                pagePedidos.getNumber(),
                pagePedidos.getSize(),
                pagePedidos.getTotalElements(),
                pagePedidos.getTotalPages()
        );
    }
}
