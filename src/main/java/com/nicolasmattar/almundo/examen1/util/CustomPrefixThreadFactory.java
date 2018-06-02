package com.nicolasmattar.almundo.examen1.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadFactory Default que permite definir el prefijo del nombre del Thread.
 * Una extension de Executors.DefaultThreadFactory {@link java.util.concurrent.Executors#defaultThreadFactory()}
 * <p>
 * NOTA: Ciertas librerias como Guava ya tienen esto implementado, pero parecio innecesario agregar toda la libreria por una unica clase.
 */
public class CustomPrefixThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public CustomPrefixThreadFactory(String namePrefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix + "-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
