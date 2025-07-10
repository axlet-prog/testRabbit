package com.example.testRabbitMQ.messaging;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerUtils {

    private final ConnectionFactory connectionFactory;

    public void declareListener(
        MessageListener listenerContainer,
        String... queues
    ) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setQueueNames(queues);
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerContainer);
        container.start();
    }

}
