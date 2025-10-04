package com.rafaelcaxixi.spring_junit;

import org.springframework.boot.SpringApplication;

public class TestSpringJunitApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringJunitApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
