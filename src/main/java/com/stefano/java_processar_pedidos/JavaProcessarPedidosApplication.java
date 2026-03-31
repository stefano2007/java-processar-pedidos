package com.stefano.java_processar_pedidos;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class JavaProcessarPedidosApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaProcessarPedidosApplication.class, args);
	}

}
