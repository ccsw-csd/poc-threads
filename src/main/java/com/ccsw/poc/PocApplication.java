package com.ccsw.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ccsw.poc.queue.pool.ConsumerPoolService;

@SpringBootApplication
@EnableScheduling
public class PocApplication implements CommandLineRunner {

    @Value("${app.async.maxThreads}")
    private int maxThreads;

    @Value("${app.pool.enabled}")
    private Boolean poolEnabled;

    @Autowired
    ConsumerPoolService consumerService;

    public static void main(String[] args) {
        SpringApplication.run(PocApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if (poolEnabled)
            initializeThreads(maxThreads);

    }

    public void initializeThreads(int numThreads) {

        for (int i = 0; i < numThreads; i++) {
            consumerService.execute();
        }

    }
}
