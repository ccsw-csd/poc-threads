package com.ccsw.poc.orquestador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.poc.queue.model.QueueJobEntity;
import com.ccsw.poc.queue.model.QueueJobRepository;

@Service
public class OrquestadorServiceImpl implements OrquestadorService {

    @Autowired
    QueueJobRepository queueJobRepository;

    @Override
    public void execute(QueueJobEntity job) {

        System.out.println("\tBloqueamos el job [" + job.getId() + "]");

        if (hasLockQueueJob(job.getId()) == false) {
            System.out.println("\tDescartamos el job [" + job.getId() + "]");
            return;
        }

        System.out.println("\tProcesamos el job [" + job.getId() + "]");

        try {

            //Thread.sleep(2000 + (int) (Math.random() * 5000f));
            Thread.sleep(7533);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean hasLockQueueJob(Long jobId) {

        int updatedRows = queueJobRepository.lockQueueJob(jobId, generateSemaphore());

        return updatedRows == 1;
    }

    private String generateSemaphore() {

        StringBuilder key = new StringBuilder();
        key.append(Thread.currentThread().getName());
        key.append("_");
        key.append(Math.random());
        key.append("_");
        key.append(Math.random());

        return key.toString();
    }

}
