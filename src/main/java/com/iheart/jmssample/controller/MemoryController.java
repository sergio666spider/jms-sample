package com.iheart.jmssample.controller;

import com.iheart.jmssample.Memory1Mb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemoryController {
    private static final Logger log = LoggerFactory.getLogger(MemoryController.class);
    private List<Memory1Mb> memories = new ArrayList<>();

    @GetMapping("/generate/{num}/sleep/{time}")
    public void generateObject(@PathVariable(value = "num") Integer num, @PathVariable(value = "time") Integer sleepTime) throws InterruptedException {
        for (int i = 0; i < num; i++) {
            memories.add(new Memory1Mb());
        }
        log.info("I need to sleep.");
        Thread.sleep(sleepTime*1000);
        log.info("Time to wake up.");
    }
}
