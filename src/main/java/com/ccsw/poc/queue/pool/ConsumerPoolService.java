package com.ccsw.poc.queue.pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ccsw.poc.orquestador.OrquestadorService;
import com.ccsw.poc.queue.model.QueueJobEntity;

@Service
public class ConsumerPoolService {

    @Autowired
    PoolQueueJobService queueJobService;

    @Autowired
    OrquestadorService orquestadorService;

    @Async
    public void execute() {

        while (true) {

            try {
                QueueJobEntity job = queueJobService.pop();
                if (job != null) {
                    orquestadorService.execute(job);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
