package com.nicolasmattar.almundo.examen1.model;

import java.time.Duration;
import java.util.Objects;

/**
 * Representa un Reporte Final de una Llamada.
 * Incluye, ademas de la llamada en cuestion, quien atendio la llamada y cuanto tiempo duro.
 */
public class CallEndReport {
    private final Call call;
    private final Employee employee;
    private final Duration duration;

    public CallEndReport(Call call, Employee employee, Duration duration) {
        this.call = call;
        this.employee = employee;
        this.duration = duration;
    }

    public Call getCall() {
        return call;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "CallEndReport{" +
                "call=" + call +
                ", employee=" + employee +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallEndReport)) return false;
        CallEndReport that = (CallEndReport) o;
        return Objects.equals(call, that.call);
    }

    @Override
    public int hashCode() {

        return Objects.hash(call);
    }
}
