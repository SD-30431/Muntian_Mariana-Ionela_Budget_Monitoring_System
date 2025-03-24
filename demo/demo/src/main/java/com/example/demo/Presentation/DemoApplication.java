package com.example.demo.Presentation;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
@EntityScan("com.example.demo.Model")
@EnableJpaRepositories("com.example.demo.Repository")
public class DemoApplication {

	public static void main(String[] args) {
		// Start the Spring Boot context and get the application context
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		// Pass the context to the JavaFX application
		JavaFXApplication.setContext(context);

		// Launch JavaFX application
		Application.launch(JavaFXApplication.class, args);
	}
}
