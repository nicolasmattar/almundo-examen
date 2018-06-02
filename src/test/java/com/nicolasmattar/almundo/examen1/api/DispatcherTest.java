package com.nicolasmattar.almundo.examen1.api;

import com.nicolasmattar.almundo.examen1.model.Call;
import com.nicolasmattar.almundo.examen1.model.CallEndReport;
import com.nicolasmattar.almundo.examen1.model.Employee;
import com.nicolasmattar.almundo.examen1.service.EmployeeService;
import com.nicolasmattar.almundo.examen1.util.EmployeeComparator;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.nicolasmattar.almundo.examen1.test.utils.LambdaUtil.rethrowREx;
import static org.assertj.core.api.Assertions.assertThat;

public class DispatcherTest {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherTest.class);

    private EmployeeService employeeService;

    /**
     * Set Up Test Case
     * <p>
     * Creamos 10 Empelados:
     * - Empleado 1 a 5 -> OPERADOR
     * - Empleado 6 a 9 -> SUPERVISOR
     * - Empleado 10    -> DIRECTOR
     */
    @Before
    public void setUp() {
        employeeService = new EmployeeService(new EmployeeComparator());
        LongStream.range(1, 6)
                .mapToObj(i -> new Employee(i, "Empleado " + i, Employee.Type.OPERADOR))
                .forEach(employeeService::addEmployee);
        LongStream.range(6, 10)
                .mapToObj(i -> new Employee(i, "Empleado " + i, Employee.Type.SUPERVISOR))
                .forEach(employeeService::addEmployee);
        employeeService.addEmployee(new Employee(10L, "Empleado 10", Employee.Type.DIRECTOR));
    }

    /**
     * Metodo Principal de Prueba requerido por Examen.
     * Ejecuta 10 Threads y llama de forma concurrente a un total de 10 Llamadas (Llamada 1 a 10).
     */
    @Test
    public void dispatchCall_main() throws InterruptedException {
        IDispatcher dispatcher = new Dispatcher(employeeService);
        this.dispatchCall(dispatcher, 10, 10);
    }

    /**
     * Extra/Plus: Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
     * <p>
     * Ejecuta 10 Threads y llama de forma concurrente a un total de 20 Llamadas (Llamada 1 a 20).
     * En este caso hay 20 Lineas Telefonicas pero solo 10 empleados, por lo que hay 10 llamadas que seran encoladas hasta que algun empleado quede libre.
     */
    @Test
    public void dispatchCall_extraCalls() throws InterruptedException {
        IDispatcher dispatcher = new Dispatcher(employeeService, 20);
        this.dispatchCall(dispatcher, 20, 10);
    }

    /**
     * Extra/Plus: Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
     * <p>
     * Ejecuta 20 Threads y llama de forma concurrente a un total de 30 Llamadas (Llamada 1 a 30).
     * En este caso hay 10 Lineas Telefonicas pero al Dispatcher se lo llama con 20 llamadas de forma concurrente, por lo que hay 20 llamadas que seran encoladas hasta que alguna linea telefonica quede libre.
     */
    @Test
    public void dispatchCall_extraThreads() throws InterruptedException {
        IDispatcher dispatcher = new Dispatcher(employeeService);
        dispatchCall(dispatcher, 30, 20);
    }

    /**
     * Metodo privado con la logica de ejecucion del test
     *
     * @param dispatcher
     * @param numberOfCalls   Numero de llamadas. Seran llamadas desde el 1 + {@code numberOfCalls + 1}
     * @param numberOfThreads Numero de Threads Concurrentes para llamar al Dispatcher
     * @throws InterruptedException Si algun thread es interrumpido.
     */
    private void dispatchCall(IDispatcher dispatcher, long numberOfCalls, int numberOfThreads) throws InterruptedException {
        //Preparar las llamadas, en este caso 40.
        Set<Callable<Future<CallEndReport>>> collect = LongStream.range(1, numberOfCalls + 1)
                .mapToObj(Call::new)
                .map(c -> (Callable<Future<CallEndReport>>) () -> dispatcher.dispatchCall(c))
                .collect(Collectors.toSet());

        //Preparar el ExecutorService con 10 Threads (Como indica el Ejercicio)
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        //Invocar todas las llamadas, el ExecutorService se ocupara de llamadar todas utilizando los 10 Threads.
        //Utilizamos parallelStream para ir imprimiendo los reportes a medida que se van completando.
        executor.invokeAll(collect)
                .parallelStream()
                .map(rethrowREx(Future::get))
                .map(rethrowREx(Future::get))
                .forEach(cr -> LOG.info(cr.toString()));
    }


    @Test
    /**
     * Test Extra: Veririca el Orden de Atencion.
     * Aunque no deterministico por las caracterisiticas Multithreading del codigo, este metodo permite verificar el orden de atencion porque llama a Dispatcher con un unico Thread.
     */
    public void dispatchCall_checkOrder() {
        //Creamos un IDispatcher con 10 Threads (Default)
        IDispatcher dispatcher = new Dispatcher(employeeService);
        //Invocamos 10 llamadas en un unico Thread para garantizar el orden de invocacion.
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Set<Future<Future<CallEndReport>>> futures = LongStream.range(1, 11)
                .mapToObj(Call::new)
                .map(c -> (Callable<Future<CallEndReport>>) () -> dispatcher.dispatchCall(c))
                .map(executor::submit)
                .collect(Collectors.toSet());
        //Esperamos que todas las llamadas finalicen
        Set<CallEndReport> callEndReport = futures.stream()
                .map(rethrowREx(Future::get))
                .map(rethrowREx(Future::get))
                .collect(Collectors.toSet());
        //Validamos que las llamadas 1 a 5 sean atendidas por un Operador, las llamadas 6 a 9 por un Supervisor y la 10 por un Director
        assertThat(callEndReport)
                .filteredOn(cr -> cr.getCall().getId() <= 5)
                .extracting(CallEndReport::getEmployee)
                .extracting(Employee::getType)
                .containsOnly(Employee.Type.OPERADOR);
        assertThat(callEndReport)
                .filteredOn(cr -> cr.getCall().getId() >= 6)
                .filteredOn(cr -> cr.getCall().getId() <= 9)
                .extracting(CallEndReport::getEmployee)
                .extracting(Employee::getType)
                .containsOnly(Employee.Type.SUPERVISOR);
        assertThat(callEndReport)
                .filteredOn(cr -> cr.getCall().getId() == 10)
                .extracting(CallEndReport::getEmployee)
                .extracting(Employee::getType)
                .containsOnly(Employee.Type.DIRECTOR);
    }
}