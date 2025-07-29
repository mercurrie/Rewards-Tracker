package com.javaproject.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	private static final Logger logger = LogManager.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Rewards Tracker Application...");
		SpringApplication.run(DemoApplication.class, args);
		logger.info("Rewards Tracker Application started successfully!");
	}
}
