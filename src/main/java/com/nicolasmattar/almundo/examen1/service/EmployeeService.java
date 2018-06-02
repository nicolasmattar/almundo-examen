package com.nicolasmattar.almundo.examen1.service;

import com.nicolasmattar.almundo.examen1.model.Employee;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class EmployeeService {

    private Queue<Employee> employees;

    public EmployeeService(Comparator<? super Employee> comparator) {
        this.employees = new PriorityBlockingQueue<>(11, comparator);
    }

    public boolean addEmployee(Employee employee) {
        return employees.add(employee);
    }

    public boolean removeEmployee(Object o) {
        return employees.remove(o);
    }

    public void removeAllEmployees() {
        employees.clear();
    }


    public Employee getNextFreeEmployee() {
        return employees.poll();
    }

    public void freeEmployee(Employee employee) {
        employees.add(employee);
    }
}
