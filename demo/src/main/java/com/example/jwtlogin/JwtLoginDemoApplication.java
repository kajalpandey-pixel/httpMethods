package com.example.jwtlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class JwtLoginDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtLoginDemoApplication.class, args);
	}

}
