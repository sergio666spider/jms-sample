package com.iheart.jmssample.controller;

import com.iheart.jmssample.Memory1Mb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.Queue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@RestController
public class JmsSampleController {
    private static List<HashMap> memory = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(JmsSampleController.class);

    private static List<Memory1Mb> memories = new ArrayList<>();

    private static Integer count = 0;

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private Queue queue;

    @RequestMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam Integer quantity) throws InterruptedException, UnknownHostException {
        for (int i = 0; i < quantity; i++){
            sendToQueue(i);
        }

        InetAddress inetAddress = InetAddress.getLocalHost();
        return ResponseEntity.ok(String.format("Sending %d messages answered by %s", quantity, inetAddress.getHostName()));
    }

    private void sendToQueue(Integer i) {
        final String message = String.format("Message #%d", i + 1);
        log.info("Sending: {}", message);
        template.convertAndSend(queue.getName(), message);
    }

    @RequestMapping("/computeSquare")
    public ResponseEntity<String> computeSquare(@RequestParam Integer quantity) throws InterruptedException, UnknownHostException {
        IntStream.range(0, quantity).parallel().forEach(
                i -> Math.sqrt(i * 1.5d)
        );

        InetAddress inetAddress = InetAddress.getLocalHost();
        return ResponseEntity.ok(String.format("hostname: %s", inetAddress.getHostName()));
    }

    @GetMapping("/increaseMemory")
    public ResponseEntity<String> increaseMemory(@RequestParam Integer quantity) throws InterruptedException, UnknownHostException {
        for (int i = 0; i < quantity; i++) {
            memories.add(new Memory1Mb());
        }

        InetAddress inetAddress = InetAddress.getLocalHost();
        return ResponseEntity.ok(String.format("hostname: %s", inetAddress.getHostName()));
    }

    @GetMapping("/cleanMemory")
    public ResponseEntity<String> increaseMemory() throws InterruptedException, UnknownHostException {
        memories.clear();

        System.gc();

        InetAddress inetAddress = InetAddress.getLocalHost();
        return ResponseEntity.ok(String.format("deleting men answered by %s", inetAddress.getHostName()));
    }

    @GetMapping("/increaseCpuMemory")
    public ResponseEntity<String> increaseCpuMemory(@RequestParam Integer quantity) throws InterruptedException, UnknownHostException {
        for (int i = 0; i < quantity; i++) {
            memories.add(new Memory1Mb());
        }

        InetAddress inetAddress = InetAddress.getLocalHost();
        return ResponseEntity.ok(String.format("hostname: %s - %d", inetAddress.getHostName(), count++)) ;
    }
}
