package org.example.currentpercentageservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class CurrentPercentageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrentPercentageServiceApplication.class, args);
	}

}
