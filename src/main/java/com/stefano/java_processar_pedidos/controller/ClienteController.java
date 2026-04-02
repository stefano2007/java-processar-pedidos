package com.stefano.java_processar_pedidos.controller;

import com.stefano.java_processar_pedidos.controller.dto.ClienteQuantidadeResponse;
import com.stefano.java_processar_pedidos.controller.dto.PagePedidoResponse;
import com.stefano.java_processar_pedidos.service.PedidoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienteController {

    private final PedidoService pedidoService;

    public ClienteController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/api/clientes/{clienteId}/pedidos/quantidade")
    public ResponseEntity<ClienteQuantidadeResponse> obterQuantidadePedidosCliente(
            @PathVariable("clienteId") Long clienteId
    ) {
        return ResponseEntity.of(pedidoService.obterQuantidadePedidosCliente(clienteId));
    }


    @GetMapping("/api/clientes/{clienteId}/pedidos")
    public ResponseEntity<PagePedidoResponse> obterPedidosCliente(
            @PathVariable("clienteId") Long clienteId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(pedidoService.obterPedidosCliente(clienteId, PageRequest.of(page, pageSize)));
    }
}
