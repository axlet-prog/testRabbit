package com.example.testRabbitMQ.api;

import com.example.testRabbitMQ.messaging.TestProducer;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestProducer producer;

    @PostMapping("/direct-send/{exchangeName}")
    public String directSendMessage(
        @PathVariable String exchangeName, @RequestParam("key") String key, @RequestParam("message") String message
    ) {
        producer.sendDirect(
            exchangeName,
            key,
            message
        );
        return message;
    }

    @PostMapping("/fanout-send")
    public String directSendMessage(@RequestParam("message") String message) {
        producer.sendFanout(message);
        return message;
    }

    @PostMapping("/topic-send")
    public String topicSendMessage(@RequestParam("key") String key, @RequestParam("message") String message) {
        producer.sendTopic(message, key);
        return message;
    }

    @PostMapping("/headers-send")
    public String headersSendMessage(@RequestParam("message") String message, @RequestBody Map<String, String> headers) {
        producer.sendHeaders(message, headers);
        return message;
    }

    @PostMapping("/exception-send")
    public String exceptionSendMessage(@RequestParam("message") String message) {
        producer.sendException(message);
        return message;
    }
}
