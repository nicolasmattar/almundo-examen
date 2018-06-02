package com.nicolasmattar.almundo.examen1.api;

import com.nicolasmattar.almundo.examen1.exception.LlamadaFinalizadaAbruptamenteException;
import com.nicolasmattar.almundo.examen1.model.Call;
import com.nicolasmattar.almundo.examen1.model.CallEndReport;
import com.nicolasmattar.almundo.examen1.model.Employee;
import com.nicolasmattar.almundo.examen1.service.EmployeeService;
import com.nicolasmattar.almundo.examen1.util.CustomPrefixThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * Implementacion default del IDispatcher.
 * Esta implementacion encola las distintas llamadas en un {@code ExecutorService}.
 * <p>
 * MAX_THREADS
 * <p>
 * En caso de que no existan empleados libres o entren mas de 10 llamadas concurrentes las llamadas seran encoladas dentro de del {@link ExecutorService} (Utiliza un {@link LinkedBlockingQueue} por dentro.
 *
 * @author Nicolas Mattar
 */
public class Dispatcher implements IDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDispatcher.class);

    /**
     * Este numero se configuraria segun la cantidad de "lineas telefonicas" disponibles.
     * Basado en la premisa del Examen, se configura un MAX_THREADS de 10.
     */
    public static final int MAX_THREADS = 10;

    private final EmployeeService employeeService;
    private final ExecutorService executor;

    /**
     * Crea un nuevo Dispatcher utilizando el EmployeeService enviado como parametro y 10 Threads (Lineas Telefonicas)
     *
     * @param employeeService Servicio de Empleados
     */
    public Dispatcher(EmployeeService employeeService) {
        this(employeeService, MAX_THREADS);
    }

    /**
     * Crea un nuevo Dispatcher utilizando el EmployeeService enviado como parametro y {@code maxThreads} Threads.
     *
     * @param employeeService Servicio de Empleados
     * @param maxThreads      Cantidad de Maxima Threads / Lineas Tefonicas
     */
    public Dispatcher(EmployeeService employeeService, int maxThreads) {
        if(employeeService == null){
            throw new IllegalArgumentException("employeeService must not be null");
        }
        this.employeeService = employeeService;
        this.executor = Executors.newFixedThreadPool(MAX_THREADS, new CustomPrefixThreadFactory("phone-lin"));
        LOGGER.info("Iniciando {} Lineas Telefonicas.", maxThreads);
    }

    /**
     * Agrega una llamda a la cola, que sera atendida en algun momento en el futuro.
     *
     * @param call llamada a encolar
     * @return Futuro de una Reporte de Llamada Finalziada.
     * @see IDispatcher
     */
    @Override
    public Future<CallEndReport> dispatchCall(Call call) {
        LOGGER.info("Nueva llamada entrante {}", call);
        return this.executor.submit(new CallDispatcher(this.employeeService, call));
    }

    /**
     * Llama al {@link ExecutorService#shutdownNow()} no admitiendo mas llamadas.
     */
    public void shutdownNow() {
        LOGGER.info("Terminando todas las llamadas y cerrando las Lineas Telefonicas.");
        executor.shutdownNow();
    }

    private static class CallDispatcher implements Callable<CallEndReport> {
        private static final Logger LOGGER = LoggerFactory.getLogger(CallDispatcher.class);

        private final EmployeeService employeeService;
        private final Call call;

        public CallDispatcher(EmployeeService employeeService, Call call) {
            this.employeeService = employeeService;
            this.call = call;
        }

        @Override
        public CallEndReport call() {
            Employee employee = null;
            try {
                //Espera a que este disponible un empleado.
                employee = this.employeeService.getNextFreeEmployee();
                LOGGER.info("Asignando {} a {}.", call, employee);
                Duration durartion = employee.assignCall(call);
                this.employeeService.freeEmployee(employee);
                CallEndReport callEndReport = new CallEndReport(call, employee, durartion);
                employee = null;
                return callEndReport;
            } catch (InterruptedException e) {
                LOGGER.warn("Se ha producido un error en la llamada {}", call);
                throw new LlamadaFinalizadaAbruptamenteException(e, call);
            } finally {
                //Al final del proceso, sea por error o no, si un empleado todavia se encuentra tomado, liberarlo.
                if (employee != null) {
                    this.employeeService.freeEmployee(employee);
                }
            }
        }
    }
}
