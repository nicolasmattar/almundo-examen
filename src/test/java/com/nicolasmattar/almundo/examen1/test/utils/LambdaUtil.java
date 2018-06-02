package com.nicolasmattar.almundo.examen1.test.utils;

import java.util.function.Function;

/**
 * Permite utilizar APIs con checked exceptions en expresiones lambdas y streams
 */
public class LambdaUtil {

    /**
     * Permite encapsular una {@code Function} que tire una checked exception y relanzar la Excepcion as {@code RuntimeException}
     * rethrowREx(future -> future.get())
     */
    public static <T, R, E extends Exception> Function<T, R> rethrowREx(FunctionWithExceptions<T, R, E> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    @FunctionalInterface
    public interface FunctionWithExceptions<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
}
