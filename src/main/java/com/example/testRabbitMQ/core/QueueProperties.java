package com.example.testRabbitMQ.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.queue")
public record QueueProperties(
    String defaultQueue1,
    String defaultQueue2,
    String directQueue1,
    String directQueue2,
    String fanoutQueue1,
    String fanoutQueue2,
    String fanoutQueue3,
    String headersQueue1,
    String headersQueue2,
    String headersQueue3,
    String topicQueue1,
    String topicQueue2,
    String topicQueue3,
    String exceptionQueue,
    String deadLettersQueue
) { }
