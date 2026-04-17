package com.pucminas.car_rental_system;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class CarRentalSystemApplicationTests {

	@Inject
	EmbeddedApplication<?> application;

	@Test
	void contextLoads() {
		assertTrue(application.isRunning());
	}
}
