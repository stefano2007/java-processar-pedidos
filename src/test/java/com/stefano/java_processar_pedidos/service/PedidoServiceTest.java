package com.stefano.java_processar_pedidos.service;

import com.stefano.java_processar_pedidos.controller.dto.ClienteQuantidadeResponse;
import com.stefano.java_processar_pedidos.controller.dto.ClienteResumoPedidoResponse;
import com.stefano.java_processar_pedidos.controller.dto.PagePedidoResponse;
import com.stefano.java_processar_pedidos.controller.dto.PedidoTotalResponse;
import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import com.stefano.java_processar_pedidos.entity.PedidoItemEntity;
import com.stefano.java_processar_pedidos.listener.dto.PedidoCriadoMessage;
import com.stefano.java_processar_pedidos.repository.PedidoRepository;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("PedidoService - Testes Unitários")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private PedidoService pedidoService;

    PedidoEntity pedidoEntityMock;

    @BeforeEach
    void preparer() {
        MockitoAnnotations.openMocks(this);
        pedidoEntityMock = mock(PedidoEntity.class);
    }

    @Test
    void deveSalvarPedidoComSucesso() {
        // Arrange
        PedidoCriadoMessage.PedidoCriadoMessageItem item =
                new PedidoCriadoMessage.PedidoCriadoMessageItem("lápis", 2, BigDecimal.valueOf(1.5));

        PedidoCriadoMessage message = new PedidoCriadoMessage(
                1L,
                10L,
                List.of(item)
        );

        // Act
        pedidoService.salvarPedido(message);

        // Assert
        verify(pedidoRepository, times(1)).save(any(PedidoEntity.class));
    }

    @Test
    void deveObterItensPedidoComSucesso() {
        // Arrange
        List<PedidoCriadoMessage.PedidoCriadoMessageItem> itens = List.of(
                new PedidoCriadoMessage.PedidoCriadoMessageItem("lápis", 2, BigDecimal.valueOf(1.5)),
                new PedidoCriadoMessage.PedidoCriadoMessageItem("caderno", 1, BigDecimal.valueOf(10))
        );

        // Act
        List<PedidoItemEntity> resultado = pedidoService.obterItensPedido(itens);

        // Assert
        Assertions.assertEquals(2, resultado.size());

        Assertions.assertEquals("lápis", resultado.get(0).getProduto());
        Assertions.assertEquals(2, resultado.get(0).getQuantidade());
        Assertions.assertEquals(BigDecimal.valueOf(1.5), resultado.get(0).getPreco());

        Assertions.assertEquals("caderno", resultado.get(1).getProduto());
        Assertions.assertEquals(1, resultado.get(1).getQuantidade());
        Assertions.assertEquals(BigDecimal.valueOf(10), resultado.get(1).getPreco());
    }

    @Test
    void deveRetornarPedidosClienteComSucesso() {
        // Arrange
        Long clienteId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);



        Page<PedidoEntity> pageMock = new PageImpl<>(List.of(pedidoEntityMock));

        when(pedidoRepository.findAllByClienteId(clienteId, pageRequest)).thenReturn(pageMock);

        // Act
        PagePedidoResponse response = pedidoService.obterPedidosCliente(clienteId, pageRequest);

        // Assert
        Assertions.assertNotNull(response);
        verify(pedidoRepository, times(1)).findAllByClienteId(clienteId, pageRequest);
    }

    @Test
    void deveRetornarTotalPedidoComSucesso() {
        // Arrange
        Long pedidoId = 1L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoEntityMock));

        // Act
        Optional<PedidoTotalResponse> response = pedidoService.obterTotalPedido(pedidoId);

        // Assert
        Assertions.assertTrue(response.isPresent());
        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    void deveRetornarTotalPedidoVazioQuandoNaoEncontrado() {
        // Arrange
        Long pedidoId = 1L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Act
        Optional<PedidoTotalResponse> response = pedidoService.obterTotalPedido(pedidoId);

        // Assert
        Assertions.assertTrue(response.isEmpty());
        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    void deveRetornarQuantidadePedidosClienteComSucesso() {
        // Arrange
        Long clienteId = 1L;

        when(pedidoRepository.countByClienteId(clienteId)).thenReturn(2);

        // Act
        Optional<ClienteQuantidadeResponse> response =
                pedidoService.obterQuantidadePedidosCliente(clienteId);

        // Assert
        Assertions.assertTrue(response.isPresent());

        ClienteQuantidadeResponse resultado = response.get();
        Assertions.assertEquals(clienteId, resultado.clienteId());
        Assertions.assertEquals(2, resultado.quantidadePedidos());

        verify(pedidoRepository, times(1)).countByClienteId(clienteId);
    }

    @Test
    void deveRetornarQuantidadePedidosClienteVazioQuandoZero() {
        // Arrange
        Long clienteId = 1L;

        when(pedidoRepository.countByClienteId(clienteId)).thenReturn(0);

        // Act
        Optional<ClienteQuantidadeResponse> response =
                pedidoService.obterQuantidadePedidosCliente(clienteId);

        // Assert
        Assertions.assertTrue(response.isEmpty());
        verify(pedidoRepository, times(1)).countByClienteId(clienteId);
    }

    @Test
    void deveRetornarResumoDePedidosPorCliente() {

        // Arrange
        ClienteResumoPedidoResponse mockResponse =
                new ClienteResumoPedidoResponse(
                        1L,
                        BigDecimal.valueOf(120.0),
                        1
                );

        List<ClienteResumoPedidoResponse> listaMock = List.of(mockResponse);

        AggregationResults<ClienteResumoPedidoResponse> aggregationResults =
                new AggregationResults<>(listaMock, new Document());

        when(mongoTemplate.aggregate(
                any(Aggregation.class),
                anyString(),
                any(Class.class)
        )).thenReturn(aggregationResults);

        // Act
        List<ClienteResumoPedidoResponse> resultado =
                pedidoService.obterClienteResumoPedidos();

        // Assert
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());

        ClienteResumoPedidoResponse item = resultado.get(0);

        Assertions.assertEquals(1L, item.id());
        Assertions.assertEquals(BigDecimal.valueOf(120.0), item.totalPedidos());
        Assertions.assertEquals(1, item.quantidadePedidos());
    }
}