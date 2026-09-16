package com.example.leavebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableAsync
@EnableRetry
@EnableRabbit
public class LeavebookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeavebookingApplication.class, args);
	}

}
