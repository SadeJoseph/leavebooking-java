package com.example.leavebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LeavebookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeavebookingApplication.class, args);
	}

}
