package com.ccsw.poc.queue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "queue_job")
public class QueueJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "num_envio", nullable = false)
    private Long numeroEnvio;

    @Column(name = "complete", nullable = false)
    private Boolean complete;

    @Column(name = "urgent", nullable = false)
    private Boolean urgent;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "semaphore", nullable = false)
    private String semaphore;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the numeroEnvio
     */
    public Long getNumeroEnvio() {
        return numeroEnvio;
    }

    /**
     * @param numeroEnvio the numeroEnvio to set
     */
    public void setNumeroEnvio(Long numeroEnvio) {
        this.numeroEnvio = numeroEnvio;
    }

    /**
     * @return the complete
     */
    public Boolean getComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    /**
     * @return the urgent
     */
    public Boolean getUrgent() {
        return urgent;
    }

    /**
     * @param urgent the urgent to set
     */
    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the semaphore
     */
    public String getSemaphore() {
        return semaphore;
    }

    /**
     * @param semaphore the semaphore to set
     */
    public void setSemaphore(String semaphore) {
        this.semaphore = semaphore;
    }

}
