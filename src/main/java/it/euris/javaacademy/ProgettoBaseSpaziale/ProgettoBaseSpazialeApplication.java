package it.euris.javaacademy.ProgettoBaseSpaziale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProgettoBaseSpazialeApplication {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "GMT");
		SpringApplication.run(ProgettoBaseSpazialeApplication.class, args);
	}

}
