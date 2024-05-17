package com.ryanmhub.fitnessapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestFitnessAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(FitnessAppApplication::main).with(TestFitnessAppApplication.class).run(args);
	}

}
