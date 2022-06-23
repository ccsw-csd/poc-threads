package com.ccsw.poc.queue.simple;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.ccsw.poc.queue.model.QueueJobEntity;
import com.ccsw.poc.queue.model.QueueJobRepository;

@Service
public class QueueJobServiceImpl implements QueueJobService {

    @Value("${app.async.maxThreads}")
    private int maxThreads;

    @Value("${app.pool.enabled}")
    private Boolean poolEnabled;

    @Autowired
    @Qualifier("executor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private QueueJobRepository queueJobRepository;

    @Autowired
    private ConsumerService consumerService;

    private long initTime;

    @Scheduled(fixedDelay = 5000)
    public void executeListenerWithCreation() {

        if (poolEnabled == true)
            return;

        if (initTime == 0) {
            initTime = System.currentTimeMillis();
        }

        System.out.println("* Listener: [" + getAvailableThreads() + "]");

        if (getAvailableThreads() > 0) {

            List<QueueJobEntity> jobs = queueJobRepository.findTop100ByCompleteAndUrgentAndStatusAndSemaphoreIsNullOrderByIdAsc(true, true, "PND");
            System.out.println("\tLeemos nuevas tareas: [" + jobs.size() + "]");

            for (QueueJobEntity job : jobs) {
                executeOrchestrator(job);

                if (getAvailableThreads() == 0)
                    break;
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - initTime) / 1000;
        System.out.println("* Listener: end [" + getAvailableThreads() + "]: - Total time (s): " + totalTime);

    }

    private void executeOrchestrator(QueueJobEntity job) {
        if (getAvailableThreads() > 0) {
            consumerService.execute(job);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getAvailableThreads() {
        return maxThreads - threadPoolTaskExecutor.getActiveCount();
    }

}
