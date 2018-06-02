package com.nicolasmattar.almundo.examen1.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa una llamada en el sistema.
 */
public class Call {

    private static final Logger LOGGER = LoggerFactory.getLogger(Call.class);

    private final Long id;

    public Call(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Duration answer() throws InterruptedException {
        LOGGER.info("{} - Iniciando llamada...", this);
        //Simulate Call Time
        long millis = ThreadLocalRandom.current().nextLong(5000, 11000);
        Thread.sleep(millis);
        LOGGER.info("{} - Llamada finalizada.", this);
        return Duration.ofMillis(millis);
    }

    @Override
    public String toString() {
        return "Call{" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Call)) return false;
        Call call = (Call) o;
        return Objects.equals(id, call.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}