package com.stefano.java_processar_pedidos.listener;

import com.stefano.java_processar_pedidos.listener.dto.PedidoCriadoMessage;
import com.stefano.java_processar_pedidos.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static com.stefano.java_processar_pedidos.config.RabbitMqConfig.PEDIDO_CRIADO_QUEUE;

@Component
public class PedidoCriadoConsumer {

    private Logger logger = Logger.getLogger(PedidoCriadoConsumer.class.getName());

    private final PedidoService pedidoService;

    public PedidoCriadoConsumer(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = PEDIDO_CRIADO_QUEUE)
    public void consumirPedidoCriado(Message<PedidoCriadoMessage> mensagemPedidoCriado) {
        logger.info("Mensagem consumida: " + mensagemPedidoCriado);

        pedidoService.salvarPedido(mensagemPedidoCriado.getPayload());
    }
}
