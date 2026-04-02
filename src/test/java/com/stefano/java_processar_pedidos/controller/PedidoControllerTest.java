package com.stefano.java_processar_pedidos.controller;

import com.stefano.java_processar_pedidos.controller.dto.PedidoTotalResponse;
import com.stefano.java_processar_pedidos.service.PedidoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@DisplayName("PedidoController - Testes Unitários")
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void preparer() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPedidoTotalComSucesso(){
        //Arrange
        Long pedidoId = 1001L;
        PedidoTotalResponse pedidoTotalResponseMock = new PedidoTotalResponse(pedidoId, BigDecimal.valueOf(1));
        when(pedidoService.obterTotalPedido(pedidoId)).thenReturn(Optional.of(pedidoTotalResponseMock));

        //Act
        ResponseEntity<PedidoTotalResponse> resultado = pedidoController.obterTotalPedido(pedidoId);

        //Assert
        Assertions.assertEquals(resultado.getStatusCode(), HttpStatusCode.valueOf(200));

        PedidoTotalResponse pedidoResponse = resultado.getBody();
        Assertions.assertNotNull(pedidoResponse);
        Assertions.assertEquals(pedidoTotalResponseMock.pedidoId(), pedidoResponse.pedidoId());
        Assertions.assertEquals(pedidoTotalResponseMock.valorTotal(), pedidoResponse.valorTotal());
    }

    @Test
    void deveRetornarPedidoTotalNaoEncontrado(){
        //Arrange
        Long pedidoId = 1001L;
        when(pedidoService.obterTotalPedido(pedidoId)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<PedidoTotalResponse> resultado = pedidoController.obterTotalPedido(pedidoId);

        //Assert
        Assertions.assertEquals(resultado.getStatusCode(), HttpStatusCode.valueOf(404));

        PedidoTotalResponse pedidoResponse = resultado.getBody();
        Assertions.assertNull(pedidoResponse);
    }
}