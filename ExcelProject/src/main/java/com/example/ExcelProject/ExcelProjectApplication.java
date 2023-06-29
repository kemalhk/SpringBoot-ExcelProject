package com.example.ExcelProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ExcelProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelProjectApplication.class, args);
	}

}
