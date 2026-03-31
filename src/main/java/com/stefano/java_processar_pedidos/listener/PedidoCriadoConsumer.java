package com.stefano.java_processar_pedidos.listener;

import com.stefano.java_processar_pedidos.listener.dto.PedidoCriadoMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.stefano.java_processar_pedidos.config.RabbitMqConfig.PEDIDO_CRIADO_QUEUE;

@Component
public class PedidoCriadoConsumer {

    @RabbitListener(queues = PEDIDO_CRIADO_QUEUE)
    public void consumirPedidoCriado(Message<PedidoCriadoMessage> pedidoCriado) {
        // Lógica para processar o pedido criado
        System.out.println("Pedido criado recebido: " + pedidoCriado.getPayload());
    }
}
