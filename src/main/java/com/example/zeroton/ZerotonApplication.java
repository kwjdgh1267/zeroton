package com.example.zeroton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ZerotonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZerotonApplication.class, args);
	}

}
