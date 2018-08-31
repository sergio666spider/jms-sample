package com.iheart.jmssample;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private Queue queue;

    @Scheduled(fixedRate = 5000)
    public void receive(){
        String message = (String) template.receiveAndConvert(queue.getName());
        if (Strings.isNotEmpty(message)) {
            log.info("Message received: {}.", message);
        } else {
            log.info("No message to process.");
        }
    }
}