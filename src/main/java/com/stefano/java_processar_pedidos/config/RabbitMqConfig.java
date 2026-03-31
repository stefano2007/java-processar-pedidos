package com.stefano.java_processar_pedidos.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PEDIDO_CRIADO_QUEUE = "pedido-criado";

    /**
     * Configura o conversor de mensagens para JSON usando Jackson. Isso permite que as mensagens sejam convertidas automaticamente para objetos Java ao serem recebidas e para JSON ao serem enviadas.
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura a fila "pedido_criados" onde as mensagens de pedidos criados serão enviadas. A anotação @Bean indica que este método retorna um bean gerenciado pelo Spring, e a classe Queue é usada para criar uma nova fila com o nome especificado.
     */
    @Bean
    public Queue pedidoCriadoQueue() {
        return new Queue(PEDIDO_CRIADO_QUEUE);
    }
}
