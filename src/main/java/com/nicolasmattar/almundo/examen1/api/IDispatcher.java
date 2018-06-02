package com.nicolasmattar.almundo.examen1.api;

import com.nicolasmattar.almundo.examen1.model.Call;
import com.nicolasmattar.almundo.examen1.model.CallEndReport;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * API Principal del Sistema, permite encolar una nueva llamada.
 * Todas sus implementaciones deben ser ThreadSafe
 */
public interface IDispatcher {

    /**
     * Metodo para despachar una llamada.
     *
     * @param call llamada a encolar
     * @return Futuro de una Reporte de Llamada Finalziada.
     */
    Future<CallEndReport> dispatchCall(Call call);
}
