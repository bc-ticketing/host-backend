package com.idetix.hostbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HostbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostbackendApplication.class, args);
	}

}
