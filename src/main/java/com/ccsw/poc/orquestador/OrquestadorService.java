package com.ccsw.poc.orquestador;

import com.ccsw.poc.queue.model.QueueJobEntity;

public interface OrquestadorService {

    void execute(QueueJobEntity job);
}
