package com.stefano.java_processar_pedidos.testeIntegrados.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class CucumberSpringConfig {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.12-management");

    // 🔥 Garante que os containers iniciem antes do Spring usar
    static {
        mongo.start();
        rabbit.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        // Mongo
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);

        // Rabbit
        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);

        // Logs úteis
        System.out.println("Mongo URI: " + mongo.getReplicaSetUrl());
        System.out.println("Rabbit Host: " + rabbit.getHost());
        System.out.println("Rabbit Port: " + rabbit.getAmqpPort());
    }
}
