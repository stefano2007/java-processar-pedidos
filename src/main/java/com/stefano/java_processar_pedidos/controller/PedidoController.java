package com.stefano.java_processar_pedidos.controller;

import com.stefano.java_processar_pedidos.controller.dto.PedidoTotalResponse;
import com.stefano.java_processar_pedidos.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/api/pedidos/{pedidoId}/total")
    public ResponseEntity<PedidoTotalResponse> obterTotalPedido(
            @PathVariable("pedidoId") Long pedidoId
    ) {
        return ResponseEntity.of(pedidoService.obterTotalPedido(pedidoId));
    }
}
