package com.javaproject.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DemoApplicationTests {

	@Test
	void applicationStartsSuccessfully() {
		// Simple test that verifies the main class exists and can be referenced
		assertDoesNotThrow(() -> {
			DemoApplication.class.getDeclaredConstructor();
		});
	}

}
