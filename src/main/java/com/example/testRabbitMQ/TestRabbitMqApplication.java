package com.example.testRabbitMQ;

import com.example.testRabbitMQ.core.QueueProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@EnableConfigurationProperties
@SpringBootApplication
@EnableScheduling
public class TestRabbitMqApplication {

	public static void main(String[] args) {
		System.out.println("New version");
		SpringApplication.run(TestRabbitMqApplication.class, args);
	}
}
