package com.stefano.java_processar_pedidos.listener;

import com.stefano.java_processar_pedidos.listener.dto.PedidoCriadoMessage;
import com.stefano.java_processar_pedidos.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("PedidoCriadoConsumer - Testes Unitários")
class PedidoCriadoConsumerTest {


    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoCriadoConsumer consumer;

    @BeforeEach
    void preparar() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConsumirMensagemEChamarService() {
        // Arrange
        PedidoCriadoMessage payload = new PedidoCriadoMessage(
                1001L,
                1L,
                List.of()
        );
        Message<PedidoCriadoMessage> mensagem =    MessageBuilder.withPayload(payload).build();

        // Act
        consumer.consumirPedidoCriado(mensagem);

        // Assert
        verify(pedidoService, times(1)).salvarPedido(payload);
    }

}