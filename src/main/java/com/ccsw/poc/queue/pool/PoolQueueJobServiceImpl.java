package com.ccsw.poc.queue.pool;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ccsw.poc.queue.model.QueueJobEntity;
import com.ccsw.poc.queue.model.QueueJobRepository;

@Service
public class PoolQueueJobServiceImpl implements PoolQueueJobService {

    @Value("${app.pool.enabled}")
    private Boolean poolEnabled;

    @Value("${app.async.maxThreads}")
    private int maxThreads;

    @Autowired
    private QueueJobRepository queueJobRepository;

    private Queue<QueueJobEntity> jobsQueue = new ArrayDeque<>();

    private long initTime;

    private Object mutex = new Object();

    @Override
    public QueueJobEntity pop() throws InterruptedException {

        synchronized (mutex) {
            if (jobsQueue.isEmpty())
                mutex.wait();

            return jobsQueue.poll();
        }

    }

    @Scheduled(fixedDelay = 5000)
    public void executeListenerBatch() {

        if (poolEnabled == false)
            return;

        if (initTime == 0) {
            initTime = System.currentTimeMillis();
        }

        System.out.println("* Listener: [" + availableJobs() + "]");

        if (availableJobs() < maxThreads * 2) {
            List<QueueJobEntity> jobs = queueJobRepository.findTop100ByCompleteAndUrgentAndStatusAndSemaphoreIsNullOrderByIdAsc(true, true, "PND");
            System.out.println("\tLeemos nuevas tareas: [" + jobs.size() + "]");

            populateQueue(jobs);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - initTime) / 1000;
        System.out.println("* Listener: end [" + availableJobs() + "]: - Total time (s): " + totalTime);

    }

    private int availableJobs() {
        synchronized (mutex) {
            return jobsQueue.size();
        }
    }

    private void populateQueue(List<QueueJobEntity> jobs) {
        synchronized (mutex) {
            jobsQueue.clear();
            jobsQueue.addAll(jobs);
            mutex.notifyAll();
        }
    }

}
