package com.stefano.java_processar_pedidos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String PEDIDOS_EXCHANGE = "pedidos.v1.exchange";
    public static final String PEDIDOS_DLX = "pedidos.v1.dlx";

    public static final String PEDIDO_CRIADO_QUEUE = "pedidos.v1.pedido-criado";
    public static final String PEDIDO_CRIADO_QUEUE_DLQ = "pedidos.v1.pedido-criado-dlq";

    @Bean
    public DirectExchange pedidosExchange() {
        return new DirectExchange(PEDIDOS_EXCHANGE);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(PEDIDOS_DLX);
    }

    /**
     * Configura o conversor de mensagens para JSON usando Jackson. Isso permite que as mensagens sejam convertidas automaticamente para objetos Java ao serem recebidas e para JSON ao serem enviadas.
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue pedidoCriadoQueue() {
        Map<String, Object> args = new HashMap<>();

        args.put("x-dead-letter-exchange", PEDIDOS_DLX);
        args.put("x-dead-letter-routing-key", PEDIDO_CRIADO_QUEUE_DLQ);

        return new Queue(PEDIDO_CRIADO_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue pedidoCriadoQueueDLQ() {
        return new Queue(PEDIDO_CRIADO_QUEUE_DLQ);
    }


    @Bean
    public Binding bindingPedidoCriadoQueue() {
        return BindingBuilder
                .bind(pedidoCriadoQueue())
                .to(pedidosExchange())
                .with(PEDIDO_CRIADO_QUEUE);
    }

    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder
                .bind(pedidoCriadoQueueDLQ())
                .to(dlxExchange())
                .with(PEDIDO_CRIADO_QUEUE_DLQ);
    }
}
