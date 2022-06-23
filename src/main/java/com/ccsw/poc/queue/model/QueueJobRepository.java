package com.ccsw.poc.queue.model;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface QueueJobRepository extends CrudRepository<QueueJobEntity, Long> {

    /**
     * Recupera los 50 primeros jobs completos sin semaforo, filtrados por urgencia y estado
     * @param complete
     * @param urgent
     * @param status
     * @return
     */
    List<QueueJobEntity> findTop100ByCompleteAndUrgentAndStatusAndSemaphoreIsNullOrderByIdAsc(Boolean complete, Boolean urgent, String status);

    /**
     * Realiza un bloqueo en BBDD de un jobId con un semaforo
     * @param jobId
     * @param semaphore
     * @return
     */
    @Modifying
    @Transactional(readOnly = false)
    @Query("update QueueJobEntity set semaphore = :semaphore where id = :jobId and semaphore is null")
    int lockQueueJob(@Param("jobId") Long jobId, @Param("semaphore") String semaphore);

}
