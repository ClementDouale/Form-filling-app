package com.utc.formut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.utc.formut.web.repositories"})
@EntityScan({"com.utc.formut.web.Models"})
@SpringBootApplication
public class FormUtApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormUtApplication.class, args);
	}

}
