package com.ccsw.poc.queue.pool;

import com.ccsw.poc.queue.model.QueueJobEntity;

public interface PoolQueueJobService {

    public QueueJobEntity pop() throws InterruptedException;

}
