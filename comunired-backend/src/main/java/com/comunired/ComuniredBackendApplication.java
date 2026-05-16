package com.comunired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ComuniredBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComuniredBackendApplication.class, args);
	}

}
