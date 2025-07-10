package com.example.testRabbitMQ.messaging;

import com.example.testRabbitMQ.core.ExchangeProperties;
import com.example.testRabbitMQ.core.QueueProperties;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ExchangeProperties exchangeProperties;
    private final QueueProperties queueProperties;

    public void sendDirect(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(
            exchange,
            routingKey,
            message
        );
    }

    public void sendFanout(String message) {
        rabbitTemplate.convertAndSend(
            exchangeProperties.fanoutExchange(),
            "fdsfsfsdsfds",
            message
        );
    }

    public void sendTopic(String message, String key) {
        rabbitTemplate.convertAndSend(
            exchangeProperties.topicExchange(),
            key,
            message
        );
    }

    public void sendHeaders(String message, Map<String, String> headers) {

        Message rabbitMessage = new Message(
            message.getBytes(StandardCharsets.UTF_8)
        );
        rabbitMessage.getMessageProperties().getHeaders().putAll(headers);


        rabbitTemplate.send(exchangeProperties.headersExchange(), "", rabbitMessage);
    }

    public void sendException(String message) {
        rabbitTemplate.convertAndSend(queueProperties.exceptionQueue(), message);
    }
}
