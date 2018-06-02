package com.nicolasmattar.almundo.examen1.service;

import com.nicolasmattar.almundo.examen1.model.Employee;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Servicio para administrar los Empleados.
 * <p>
 * Esta implementacion permite administrar a los empleados disponibles para atender llamadas.
 */
public class EmployeeService {

    private final BlockingQueue<Employee> employees;

    public EmployeeService(Comparator<? super Employee> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("'comparator' no debe ser null.");
        }
        this.employees = new PriorityBlockingQueue<>(11, comparator);
    }

    /**
     * Permite agregar un Empleado al Sistema.
     * En esta implementacion en memoria solo agrega al empleado a la cola de empleados libres.
     *
     * @param employee empleado a agregar
     * @return {@code true} si fue posible agregar al empleado
     */
    public boolean addEmployee(Employee employee) {
        return employees.add(employee);
    }

    /**
     * Devuelve el proximo empleado libre o espera a que haya uno disponible.
     *
     * @return siguiente empleado libre.
     * @throws InterruptedException Si el Thread es interrumpido mientras se espera a un empleado disponible.
     */
    public Employee getNextFreeEmployee() throws InterruptedException {
        return employees.take();
    }

    /**
     * Libera a un empleado para que pueda atender otra llamada.
     *
     * @param employee empleado a liberar
     */
    public void freeEmployee(Employee employee) {
        employees.add(employee);
    }
}
