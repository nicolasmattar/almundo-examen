package com.nicolasmattar.almundo.examen1.util;

import com.nicolasmattar.almundo.examen1.model.Employee;

import java.util.Comparator;

/**
 * Compara un Empleado basado en Type.
 * <p>
 * Se respeta el siguiente orden:
 * - OPERADOR
 * - SUPERVISOR
 * - DIRECTOR
 */
public class EmployeeComparator implements Comparator<Employee> {

    public int compare(Employee o1, Employee o2) {
        return o1.getType().compareTo(o2.getType());
    }
}
