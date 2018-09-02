package com.iheart.jmssample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {
    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    public void receiveMessage(String message) throws InterruptedException {
        log.info("Received: {}", message);
        Thread.sleep(1000);
    }
}