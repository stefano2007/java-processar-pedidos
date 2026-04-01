package com.stefano.java_processar_pedidos.service;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import com.stefano.java_processar_pedidos.entity.PedidoItemEntity;
import com.stefano.java_processar_pedidos.listener.dto.PedidoCriadoMessage;
import com.stefano.java_processar_pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public void salvarPedido(PedidoCriadoMessage pedidoCriadoMessage) {
        PedidoEntity pedido = new PedidoEntity(pedidoCriadoMessage.codigoPedido(), pedidoCriadoMessage.codigoCliente(),
                obterItensPedido(pedidoCriadoMessage.itens()));

        pedidoRepository.save(pedido);
    }

    public List<PedidoItemEntity> obterItensPedido(List<PedidoCriadoMessage.PedidoCriadoMessageItem> itensPedido) {
        return itensPedido.stream()
                .map(item -> new PedidoItemEntity(item.produto(), item.quantidade(), item.preco()))
                .toList();
    }
}
