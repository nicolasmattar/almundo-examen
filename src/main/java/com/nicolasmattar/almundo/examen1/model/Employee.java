package com.nicolasmattar.almundo.examen1.model;

import java.util.Objects;

public class Employee {

    public enum Type {
        OPERADOR,
        SUPERVISOR,
        DIRECTOR
    }

    private Long legajo;
    private String nombre;
    private Type type;

    public Employee(Long legajo, String nombre, Type type) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.type = type;
    }

    public Long getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return legajo.toString() + '-' + nombre + '[' + type + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(legajo, employee.legajo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(legajo);
    }
}
