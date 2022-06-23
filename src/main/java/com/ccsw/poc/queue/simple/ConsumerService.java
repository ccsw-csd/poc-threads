package com.ccsw.poc.queue.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ccsw.poc.orquestador.OrquestadorService;
import com.ccsw.poc.queue.model.QueueJobEntity;

@Service
public class ConsumerService {

    @Autowired
    OrquestadorService orquestadorService;

    @Async
    public void execute(QueueJobEntity job) {
        orquestadorService.execute(job);
    }

}
