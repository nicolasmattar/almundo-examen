package com.nicolasmattar.almundo.examen1.exception;

import com.nicolasmattar.almundo.examen1.model.Call;

/**
 * Representa que una llamada
 * <p>
 * Eso puede suceder si un thread es interrumpido mientras espera un empleado para atenderla o la mientras la llamada esta en curso.
 */
public class LlamadaFinalizadaAbruptamenteException extends RuntimeException {

    private Call call;

    public LlamadaFinalizadaAbruptamenteException(Call call) {
        this.call = call;
    }

    public LlamadaFinalizadaAbruptamenteException(String message, Call call) {
        super(message);
        this.call = call;
    }

    public LlamadaFinalizadaAbruptamenteException(String message, Throwable cause, Call call) {
        super(message, cause);
        this.call = call;
    }

    public LlamadaFinalizadaAbruptamenteException(Throwable cause, Call call) {
        super(cause);
        this.call = call;
    }

    public LlamadaFinalizadaAbruptamenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Call call) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.call = call;
    }

    public Call getCall() {
        return call;
    }
}
