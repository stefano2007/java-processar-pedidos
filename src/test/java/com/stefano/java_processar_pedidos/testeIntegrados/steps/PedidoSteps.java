package com.stefano.java_processar_pedidos.testeIntegrados.steps;

import com.stefano.java_processar_pedidos.entity.PedidoEntity;
import com.stefano.java_processar_pedidos.entity.PedidoItemEntity;
import com.stefano.java_processar_pedidos.listener.dto.PedidoCriadoMessage;
import com.stefano.java_processar_pedidos.repository.PedidoRepository;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.List;

import static com.stefano.java_processar_pedidos.config.RabbitMqConfig.PEDIDO_CRIADO_QUEUE;
import static org.hamcrest.Matchers.*;

public class PedidoSteps {

    @LocalServerPort
    private int port;

    private Response response;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PedidoRepository pedidoRepository;

    private PedidoCriadoMessage mensagem;

    @Dado("que existe um pedido criado")
    public void criarPedido() {
        mensagem = new PedidoCriadoMessage(
                1000L,
                1000L,
                List.of()
        );
    }

    @Quando("a mensagem é enviada para a fila")
    public void enviarMensagem() {
        rabbitTemplate.convertAndSend(PEDIDO_CRIADO_QUEUE, mensagem);
    }

    @Então("o pedido deve ser salvo no banco")
    public void validarPersistencia() throws InterruptedException {
        Thread.sleep(2000);

        Long pedidoId = 1000L;

        PedidoEntity pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow();

        Assertions.assertEquals(pedidoId, pedido.getPedidoId());
        Assertions.assertEquals(pedidoId, pedido.getClienteId());
        Assertions.assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
        Assertions.assertEquals(0, pedido.getItens().size());
    }


    @Dado("que existe um pedido com id {long}")
    public void criarPedido(Long pedidoId) {
        PedidoEntity pedido = new PedidoEntity(pedidoId, 1L,
                List.of(
                        new PedidoItemEntity("lápis", 100, BigDecimal.valueOf(1.10)),
                        new PedidoItemEntity("caderno", 10, BigDecimal.valueOf(1.00))
                ));

        pedidoRepository.save(pedido);
    }

    @Dado("que existe pedido para cliente {long}")
    public void criarPedidoCliente(Long clienteId) {
        PedidoEntity pedido = new PedidoEntity(1001L, clienteId,
                List.of(
                        new PedidoItemEntity("lápis", 100, BigDecimal.valueOf(1.10)),
                        new PedidoItemEntity("caderno", 10, BigDecimal.valueOf(1.00))
                ));

        pedidoRepository.save(pedido);
    }

    @Quando("consultar o total do pedido {long}")
    public void consultarTotal(Long pedidoId) {
        response = RestAssured
                .given()
                .port(port)
                .when()
                .get("/api/pedidos/{id}/total", pedidoId);
    }

    @Quando("consultar quantidade de pedidos do cliente {long}")
    public void consultarQuantidade(Long clienteId) {
        response = RestAssured
                .given()
                .port(port)
                .when()
                .get("/api/clientes/{id}/pedidos/quantidade", clienteId);
    }

    @Quando("consultar pedidos do cliente {long} pagina {int} tamanho {int}")
    public void consultarPedidos(Long clienteId, Integer page, Integer size) {
        response = RestAssured
                .given()
                .port(port)
                .queryParam("page", page)
                .queryParam("pageSize", size)
                .when()
                .get("/api/clientes/{id}/pedidos", clienteId);
    }

    @Então("o status deve ser {int}")
    public void validarStatus(int status) {
        response.then().statusCode(status);
    }

    @Então("o valor total deve ser {string}")
    public void validarTotal(String valor) {
        BigDecimal esperado = new BigDecimal(valor);

        BigDecimal atual = response.jsonPath()
                .getObject("valorTotal", BigDecimal.class);

        Assertions.assertEquals(0, esperado.compareTo(atual));
    }

    @Então("a quantidade de pedidos deve ser {int}")
    public void validarQuantidade(int quantidade) {
        response.then()
                .body("quantidadePedidos", equalTo(quantidade));
    }

    @Então("deve retornar lista com {int} pedidos")
    public void validarLista(int quantidade) {
        response.then()
                .body("content.size()", equalTo(quantidade));
    }

    @Então("deve retornar lista vazia")
    public void validarListaVazia() {
        response.then()
                .body("content.size()", equalTo(0));
    }

    @Quando("eu chamar o endpoint de resumo de pedidos")
    public void chamarEndpointResumoPedidos() {
        response = RestAssured
                .given()
                .port(port)
                .when()
                .get("/api/clientes/resumo-pedidos")
                .then()
                .extract()
                .response();
    }

    @Então("deve retornar a lista de resumo de pedidos")
    public void validarResposta() {
        response.then()
                .body("[0].clienteId", equalTo(1))
                .body("[0].totalPedidos", equalTo(120.00F))
                .body("[0].quantidadePedidos", equalTo(1));
    }

}