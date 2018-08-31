package com.iheart.jmssample;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    public void receiveMessage(String message) throws InterruptedException {
        System.out.println("Received <" + message + ">");
        Thread.sleep(1000);
    }

}