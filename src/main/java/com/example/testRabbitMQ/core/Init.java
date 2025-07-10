package com.example.testRabbitMQ.core;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {

    private final QueueProperties queueProperties;
    private final ExchangeProperties exchangeProperties;
    private final AmqpAdmin amqpAdmin;

    // "<publication_type>.<theme>.<publication_time>"
    private final String topicRoutingKey1 = "post.#";
    private final String topicRoutingKey2 = "*.sport.#";
    private final String topicRoutingKey3 = "#.economics.new";

    private final String deadLetterKey = "dead.letter";

    @PostConstruct
    private void init() {
        initDLX();
        initDefault();
        initDirect();
        initFanout();
        initTopic();
        initHeaders();
    }

    private void initDefault() {
        Queue defaultQueue1 = QueueBuilder.durable(queueProperties.defaultQueue1()).build();
        Queue defaultQueue2 = QueueBuilder.durable(queueProperties.defaultQueue2()).build();

        amqpAdmin.declareQueue(defaultQueue1);
        amqpAdmin.declareQueue(defaultQueue2);
    }

    private void initDirect() {
        String key1 = "key1";
        String key2 = "key2";

        Queue queue1 = QueueBuilder.durable(queueProperties.directQueue1()).build();
        Queue queue2 = QueueBuilder.durable(queueProperties.directQueue2()).build();

        Exchange exchange1 = ExchangeBuilder
            .directExchange(exchangeProperties.directExchange1())
            .durable(true)
            .build();

        Exchange exchange2 = ExchangeBuilder
            .directExchange(exchangeProperties.directExchange2())
            .durable(true)
            .build();

        amqpAdmin.declareExchange(exchange1);
        amqpAdmin.declareExchange(exchange2);

        amqpAdmin.declareQueue(queue1);
        amqpAdmin.declareQueue(queue2);

        Binding binding11 = BindingBuilder.bind(queue1).to(exchange1).with(key1).noargs();
        Binding binding12 = BindingBuilder.bind(queue2).to(exchange1).with(key2).noargs();

        Binding binding21 = BindingBuilder.bind(queue1).to(exchange2).with(key1).noargs();
        Binding binding22 = BindingBuilder.bind(queue2).to(exchange2).with(key2).noargs();

        amqpAdmin.declareBinding(binding11);
        amqpAdmin.declareBinding(binding12);
        amqpAdmin.declareBinding(binding21);
        amqpAdmin.declareBinding(binding22);
    }

    public void initFanout() {
        FanoutExchange fanoutExchange = ExchangeBuilder
            .fanoutExchange(exchangeProperties.fanoutExchange())
            .durable(true)
            .build();

        amqpAdmin.declareExchange(fanoutExchange);

        Queue fanoutQueue1 = QueueBuilder.durable(queueProperties.fanoutQueue1()).build();
        Queue fanoutQueue2 = QueueBuilder.durable(queueProperties.fanoutQueue2()).build();
        Queue fanoutQueue3 = QueueBuilder.durable(queueProperties.fanoutQueue3()).build();

        amqpAdmin.declareQueue(fanoutQueue1);
        amqpAdmin.declareQueue(fanoutQueue2);
        amqpAdmin.declareQueue(fanoutQueue3);

        amqpAdmin.declareBinding(BindingBuilder.bind(fanoutQueue1).to(fanoutExchange));
        amqpAdmin.declareBinding(BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
        amqpAdmin.declareBinding(BindingBuilder.bind(fanoutQueue3).to(fanoutExchange));
    }

    public void initTopic() {
        TopicExchange topicExchange = ExchangeBuilder.topicExchange(exchangeProperties.topicExchange()).build();

        amqpAdmin.declareExchange(topicExchange);

        Queue topicQueue1 = QueueBuilder.durable(queueProperties.topicQueue1()).build();
        Queue topicQueue2 = QueueBuilder.durable(queueProperties.topicQueue2()).build();
        Queue topicQueue3 = QueueBuilder.durable(queueProperties.topicQueue3()).build();

        amqpAdmin.declareQueue(topicQueue1);
        amqpAdmin.declareQueue(topicQueue2);
        amqpAdmin.declareQueue(topicQueue3);

        amqpAdmin.declareBinding(BindingBuilder.bind(topicQueue1).to(topicExchange).with(topicRoutingKey1));
        amqpAdmin.declareBinding(BindingBuilder.bind(topicQueue2).to(topicExchange).with(topicRoutingKey2));
        amqpAdmin.declareBinding(BindingBuilder.bind(topicQueue3).to(topicExchange).with(topicRoutingKey3));

    }

    public void initHeaders() {
        HeadersExchange exchange = ExchangeBuilder.headersExchange(exchangeProperties.headersExchange()).build();

        amqpAdmin.declareExchange(exchange);

        Queue headersQueue1 = QueueBuilder.durable(queueProperties.headersQueue1()).build();
        Queue headersQueue2 = QueueBuilder.durable(queueProperties.headersQueue2()).build();
        Queue headersQueue3 = QueueBuilder.durable(queueProperties.headersQueue3()).build();

        amqpAdmin.declareQueue(headersQueue1);
        amqpAdmin.declareQueue(headersQueue2);
        amqpAdmin.declareQueue(headersQueue3);

        Map<String, Object> headers1 = new HashMap<>();
        headers1.put("header-1", "a");
        Map<String, Object> headers2 = new HashMap<>();
        headers2.put("header-2", "b");
        headers2.put("header-3", "c");

        Map<String, Object> headers3 = new HashMap<>();
        headers3.put("header-1", "d");
        headers3.put("header-3", "c");

        Binding binding1 = BindingBuilder.bind(headersQueue1).to(exchange).whereAll(headers1).match();
        Binding binding2 = BindingBuilder.bind(headersQueue2).to(exchange).whereAll(headers2).match();
        Binding binding3 = BindingBuilder.bind(headersQueue3).to(exchange).whereAny(headers3).match();

        amqpAdmin.declareBinding(binding1);
        amqpAdmin.declareBinding(binding2);
        amqpAdmin.declareBinding(binding3);
    }

    public void initDLX() {
        Queue dlq = QueueBuilder.durable(queueProperties.deadLettersQueue()).build();
        amqpAdmin.declareQueue(dlq);

        Exchange deadExchange = ExchangeBuilder.directExchange(exchangeProperties.deadExchange()).build();
        amqpAdmin.declareExchange(deadExchange);

        Queue exceptionQueue = QueueBuilder
            .durable(queueProperties.exceptionQueue())
            .deadLetterExchange(exchangeProperties.deadExchange())
            .deadLetterRoutingKey(deadLetterKey)
            .ttl(1000)
            .build();
        amqpAdmin.declareQueue(exceptionQueue);

        Binding binding = BindingBuilder.bind(dlq).to(deadExchange).with(deadLetterKey).noargs();

        amqpAdmin.declareBinding(binding);
    }
}
