package com.example.testRabbitMQ.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.exchange")
public record ExchangeProperties(
    String directExchange1,
    String directExchange2,
    String fanoutExchange,
    String headersExchange,
    String topicExchange,
    String deadExchange
) { }
