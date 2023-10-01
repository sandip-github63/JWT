package com.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		
		System.out.println("inside main method");
		SpringApplication.run(JwtApplication.class, args);
		
	}

}
