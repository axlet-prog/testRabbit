package com.example.testRabbitMQ.messaging;

import com.example.testRabbitMQ.core.QueueProperties;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestConsumer {

    private final QueueProperties queueProperties;
    private final ConsumerUtils consumerUtils;
    private final ProjectInfoAutoConfiguration projectInfoAutoConfiguration;

    @PostConstruct
    public void registerDefaultConsumer() {
        String[] queues = { queueProperties.defaultQueue1(), queueProperties.defaultQueue2() };
        consumerUtils.declareListener(
            (message) -> {
                System.out.println(
                    "Default message received: " + new String(message.getBody(), StandardCharsets.UTF_8));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            },
            queues
        );
    }

    @PostConstruct
    public void registerFanoutConsumers() {
        String[] queues = {
            queueProperties.fanoutQueue1(),
            queueProperties.fanoutQueue2(),
            queueProperties.fanoutQueue3()
        };
        int i = 1;
        for (String queue : queues) {
            int finalI = i;
            consumerUtils.declareListener(
                (message -> {
                    System.out.println(
                        "Consumer " + finalI + " consumed: " + new String(message.getBody(), StandardCharsets.UTF_8)
                    );
                }),
                queue
            );
            i++;
        }
    }

    @PostConstruct
    public void registerTopicConsumers() {
        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed topic 1: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.topicQueue1()
        );

        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed topic 2: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.topicQueue2()
        );

        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed topic 3: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.topicQueue3()
        );
    }

    @PostConstruct
    public void registerHeadersConsumers() {
        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed headers 1: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.headersQueue1()
        );

        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed headers 2: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.headersQueue2()
        );

        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed headers 3: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.headersQueue3()
        );
    }

    @PostConstruct
    public void registerExceptionConsumer() {
        consumerUtils.declareListener(
            (message -> {
                throw new RuntimeException("planned exception");
            }),
            queueProperties.exceptionQueue()
        );
    }

    @PostConstruct
    public void registerDeadLetterConsumer() {
        consumerUtils.declareListener(
            (message -> {
                System.out.println("Consumed dead: " + new String(message.getBody(), StandardCharsets.UTF_8));
            }),
            queueProperties.deadLettersQueue()
        );
    }
}