package com.nicolasmattar.almundo.examen1.util;

import com.nicolasmattar.almundo.examen1.model.Employee;
import org.junit.Before;
import org.junit.Test;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeComparatorTest {

    private Employee operador, supervisor, director;

    @Before
    public void setup() {
        operador = new Employee(1L, "Test 1", Employee.Type.OPERADOR);
        supervisor = new Employee(2L, "Test 2", Employee.Type.SUPERVISOR);
        director = new Employee(3L, "Test 3", Employee.Type.DIRECTOR);
    }

    @Test
    public void compare() {
        EmployeeComparator employeeComparator = new EmployeeComparator();

        assertThat(employeeComparator.compare(operador, operador) == 0);
        assertThat(employeeComparator.compare(supervisor, supervisor) == 0);
        assertThat(employeeComparator.compare(director, director) == 0);

        assertThat(employeeComparator.compare(operador, supervisor) == -1);
        assertThat(employeeComparator.compare(supervisor, director) == -1);
        assertThat(employeeComparator.compare(operador, director) == -1);

        assertThat(employeeComparator.compare(director, supervisor) == 1);
        assertThat(employeeComparator.compare(supervisor, operador) == 1);
        assertThat(employeeComparator.compare(director, operador) == 1);
    }

    @Test
    public void orderList() {
        SortedSet<Employee> employees = new TreeSet<>(new EmployeeComparator());
        employees.add(operador);
        employees.add(director);
        employees.add(supervisor);

        assertThat(employees)
                .hasSize(3)
                .containsExactly(operador, supervisor, director);
    }

    @Test
    public void orderQueue() {
        Queue<Employee> employees = new PriorityQueue<>(new EmployeeComparator());
        employees.add(operador);
        employees.add(director);
        employees.add(supervisor);

        assertThat(employees).hasSize(3);
        assertThat(employees.poll()).isEqualTo(operador);
        assertThat(employees).hasSize(2);
        assertThat(employees.poll()).isEqualTo(supervisor);
        assertThat(employees).hasSize(1);
        assertThat(employees.poll()).isEqualTo(director);
        assertThat(employees).hasSize(0);
    }
}