package com.alkemy.javaNivel2.TrabajoIntegrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TrabajoIntegradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrabajoIntegradorApplication.class, args);
	}

}
