package com.healthconnect.finalbackendcapstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.healthconnect.finalbackendcapstone.repository")
@EntityScan(basePackages = "com.healthconnect.finalbackendcapstone.model")
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
public class FinalBackendCapstoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalBackendCapstoneApplication.class, args);
	}

}
