package com.nicolasmattar.almundo.examen1.service;

import com.nicolasmattar.almundo.examen1.model.Employee;
import com.nicolasmattar.almundo.examen1.util.EmployeeComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;


public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        employeeService = new EmployeeService(new EmployeeComparator());

        LongStream.range(1, 6)
                .mapToObj(i -> new Employee(i, "Empleado " + i, Employee.Type.OPERADOR))
                .forEach(employeeService::addEmployee);
        LongStream.range(6, 10)
                .mapToObj(i -> new Employee(i, "Empleado " + i, Employee.Type.SUPERVISOR))
                .forEach(employeeService::addEmployee);
        employeeService.addEmployee(new Employee(10L, "Empleado 10", Employee.Type.DIRECTOR));
    }

    @Test
    public void getNextFreeEmployee_withoutFreeing() throws InterruptedException {
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.SUPERVISOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.SUPERVISOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.SUPERVISOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.SUPERVISOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.DIRECTOR);
    }

    @Test
    public void getNextFreeEmployee_withFreeing() throws InterruptedException {

        //Obtengo los 2 primetos Empleados Libres
        Employee first = employeeService.getNextFreeEmployee();
        Employee second = employeeService.getNextFreeEmployee();

        //Valido el orden de los primeros 6 empleados
        assertThat(first.getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(second.getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.OPERADOR);
        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.SUPERVISOR);

        //Libero al segundo empleado que obtuve
        employeeService.freeEmployee(second);
        //Vuelvo a sacar un empleado, como solo hay un OPERADOR disponible, deberia ser 'second'
        assertThat(employeeService.getNextFreeEmployee()).isEqualTo(second);

        assertThat(employeeService.getNextFreeEmployee().getType()).isEqualTo(Employee.Type.SUPERVISOR);

        //Libero al primer empleado que obtuve
        employeeService.freeEmployee(first);
        //Vuelvo a sacar un empleado, como solo hay un OPERADOR disponible, deberia ser 'first'
        assertThat(employeeService.getNextFreeEmployee()).isEqualTo(first);

    }
}