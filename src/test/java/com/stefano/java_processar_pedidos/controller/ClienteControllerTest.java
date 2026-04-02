package com.stefano.java_processar_pedidos.controller;

import com.stefano.java_processar_pedidos.controller.dto.ClienteQuantidadeResponse;
import com.stefano.java_processar_pedidos.controller.dto.PagePedidoResponse;
import com.stefano.java_processar_pedidos.controller.dto.PedidoResponse;
import com.stefano.java_processar_pedidos.service.PedidoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@DisplayName("ClienteController - Testes Unitários")
class ClienteControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void preparer() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarClienteQuantidadeComSucesso() {
        //Arrange
        Long clienteId = 1L;
        ClienteQuantidadeResponse clienteQuantidadeResponseMock = new ClienteQuantidadeResponse(clienteId, 1);
        when(pedidoService.obterQuantidadePedidosCliente(clienteId)).thenReturn(Optional.of(clienteQuantidadeResponseMock));

        //Act
        ResponseEntity<ClienteQuantidadeResponse> resultado = clienteController.obterQuantidadePedidosCliente(clienteId);

        //Assert
        Assertions.assertEquals(resultado.getStatusCode(), HttpStatusCode.valueOf(200));

        ClienteQuantidadeResponse clienteQuantidadeResponse = resultado.getBody();
        Assertions.assertNotNull(clienteQuantidadeResponse);
        Assertions.assertEquals(clienteQuantidadeResponseMock.clienteId(), clienteQuantidadeResponse.clienteId());
        Assertions.assertEquals(clienteQuantidadeResponseMock.quantidadePedidos(), clienteQuantidadeResponse.quantidadePedidos());
    }

    @Test
    void deveRetornarClienteQuantidadeNaoEncontrado() {
        //Arrange
        Long clienteId = 1L;
        when(pedidoService.obterQuantidadePedidosCliente(clienteId)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<ClienteQuantidadeResponse> resultado = clienteController.obterQuantidadePedidosCliente(clienteId);

        //Assert
        Assertions.assertEquals(resultado.getStatusCode(), HttpStatusCode.valueOf(404));

        ClienteQuantidadeResponse clienteQuantidadeResponse = resultado.getBody();
        Assertions.assertNull(clienteQuantidadeResponse);
    }

    @Test
    void deveRetornarPedidosClienteComSucesso() {
        //Arrange
        Long clienteId = 1L;
        Integer page = 0;
        Integer pageSize = 10;

        PagePedidoResponse clienteQuantidadeResponseMock = new PagePedidoResponse(
                List.of(
                        new PedidoResponse(
                                1001L,
                                BigDecimal.valueOf(120),
                                List.of(
                                        new PedidoResponse.ItemResponse("lápis", 100, BigDecimal.valueOf(1.1)),
                                        new PedidoResponse.ItemResponse("caderno", 10, BigDecimal.valueOf(1))
                                )
                        )
                ),
                page,
                pageSize,
                1L,
                1
        );
        when(pedidoService.obterPedidosCliente(clienteId, PageRequest.of(page, pageSize))).thenReturn(clienteQuantidadeResponseMock);

        //Act
        ResponseEntity<PagePedidoResponse> resultado = clienteController.obterPedidosCliente(clienteId, page, pageSize);

        //Assert
        Assertions.assertEquals(resultado.getStatusCode(), HttpStatusCode.valueOf(200));

        PagePedidoResponse clienteQuantidadeResponse = resultado.getBody();


        Assertions.assertEquals(page, clienteQuantidadeResponse.page());
        Assertions.assertEquals(pageSize, clienteQuantidadeResponse.pageSize());
        Assertions.assertEquals(1L, clienteQuantidadeResponse.totalElements());
        Assertions.assertEquals(1, clienteQuantidadeResponse.totalPages());


        List<PedidoResponse> pedidoResponses = clienteQuantidadeResponse.content();

        Assertions.assertEquals(1, pedidoResponses.size());
        Assertions.assertEquals(1001L, pedidoResponses.get(0).pedidoId());
        Assertions.assertEquals(BigDecimal.valueOf(120), pedidoResponses.get(0).valorTotal());

        List<PedidoResponse.ItemResponse> itens = pedidoResponses.get(0).itens();

        Assertions.assertEquals(2, itens.size());

        Assertions.assertEquals("lápis", itens.get(0).produto());
        Assertions.assertEquals(100, itens.get(0).quantidade());
        Assertions.assertEquals(BigDecimal.valueOf(1.1), itens.get(0).preco());

        Assertions.assertEquals("caderno", itens.get(1).produto());
        Assertions.assertEquals(10, itens.get(1).quantidade());
        Assertions.assertEquals(BigDecimal.valueOf(1), itens.get(1).preco());
    }

    @Test
    void deveRetornarPedidosClienteNaoEncontratoContentVazio() {
        //Arrange
        Long clienteId = 1L;
        Integer page = 0;
        Integer pageSize = 10;

        PagePedidoResponse clienteQuantidadeResponseMock = new PagePedidoResponse(
                List.of(),
                page,
                pageSize,
                0L,
                0
        );
        when(pedidoService.obterPedidosCliente(clienteId, PageRequest.of(page, pageSize))).thenReturn(clienteQuantidadeResponseMock);

        //Act
        ResponseEntity<PagePedidoResponse> resultado = clienteController.obterPedidosCliente(clienteId, page, pageSize);

        //Assert
        Assertions.assertEquals(resultado.getStatusCode(), HttpStatusCode.valueOf(200));

        PagePedidoResponse clienteQuantidadeResponse = resultado.getBody();


        Assertions.assertEquals(page, clienteQuantidadeResponse.page());
        Assertions.assertEquals(pageSize, clienteQuantidadeResponse.pageSize());
        Assertions.assertEquals(0L, clienteQuantidadeResponse.totalElements());
        Assertions.assertEquals(0, clienteQuantidadeResponse.totalPages());


        List<PedidoResponse> pedidoResponses = clienteQuantidadeResponse.content();

        Assertions.assertEquals(0, pedidoResponses.size());
    }
}