package com.example.dextra.potter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PotterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PotterApplication.class, args);
	}

}
