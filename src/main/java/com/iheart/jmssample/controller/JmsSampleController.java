package com.iheart.jmssample.controller;

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

    public static List<HashMap> memory = new ArrayList<>();

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private Queue queue;

    @RequestMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam Integer quantity) throws InterruptedException {
        IntStream.range(0, quantity).parallel().forEach(
                nbr -> sendToQueue(nbr)
        );

        return ResponseEntity.ok("Sending " + quantity + " messages.");
    }

    private void sendToQueue(Integer i) {
        System.out.println("Sending message...");
        template.convertAndSend(queue.getName(), "Message " + i);

    }

    @RequestMapping("/computeSquare")
    public ResponseEntity<String> computeSquare(@RequestParam Integer quantity) throws InterruptedException {
        IntStream.range(0, quantity).forEach(
                i -> Math.sqrt(i * 1.5d)
        );

        return ResponseEntity.ok("Sending " + quantity + " messages.");
    }


}
