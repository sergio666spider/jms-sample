package com.iheart.jmssample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.core.Queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@RestController
public class JmsSampleController {
    private static List<HashMap> memory = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(JmsSampleController.class);

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private Queue queue;

    @RequestMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam Integer quantity) throws InterruptedException {
        for (int i = 0; i < quantity; i++){
            sendToQueue(i);
        }

        return ResponseEntity.ok(String.format("Sending %d messages.", quantity));
    }

    private void sendToQueue(Integer i) {
        final String message = String.format("Message #%d", i + 1);
        log.info("Sending: {}", message);
        template.convertAndSend(queue.getName(), message);
    }

    @RequestMapping("/computeSquare")
    public ResponseEntity<String> computeSquare(@RequestParam Integer quantity) throws InterruptedException {
        IntStream.range(0, quantity).forEach(
                i -> Math.sqrt(i * 1.5d)
        );

        return ResponseEntity.ok(String.format("Sending %d messages.", quantity));
    }
}
