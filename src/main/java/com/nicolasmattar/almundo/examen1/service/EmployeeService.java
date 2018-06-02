package com.nicolasmattar.almundo.examen1.service;

import com.nicolasmattar.almundo.examen1.model.Employee;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Servicio para administrar los Empleados.
 */
public class EmployeeService {

    private final Queue<Employee> employees;

    public EmployeeService(Comparator<? super Employee> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("'comparator' no debe ser null.");
        }
        this.employees = new PriorityBlockingQueue<>(11, comparator);
    }

    public boolean addEmployee(Employee employee) {
        return employees.add(employee);
    }

    public boolean removeEmployee(Employee employee) {
        return employees.remove(employee);
    }

    public void removeAllEmployees() {
        employees.clear();
    }

    /**
     * Devuelve el proximo empleado libre o espera a que haya uno disponible.
     *
     * @return siguiente empleado libre.
     */
    public Employee getNextFreeEmployee() {
        return employees.poll();
    }

    /**
     * Libera a un empleado para que pueda atender otra llamada.
     *
     * @param employee
     */
    public void freeEmployee(Employee employee) {
        employees.add(employee);
    }
}
