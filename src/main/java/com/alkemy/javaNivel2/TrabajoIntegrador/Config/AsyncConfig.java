package com.alkemy.javaNivel2.TrabajoIntegrador.Config; // Asegúrate de crear este paquete

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Número mínimo de hilos en el pool
        executor.setMaxPoolSize(10); // Número máximo de hilos en el pool
        executor.setQueueCapacity(25); // Capacidad de la cola para tareas pendientes
        executor.setThreadNamePrefix("LibroService-Async-");
        executor.initialize();
        return executor;
    }
}
