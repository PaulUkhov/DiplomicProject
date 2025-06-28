package org.example.diplomicproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Diplomic Project API",
                version = "1.0",
                description = "Документация REST API дипломного проекта",
                contact = @Contact(
                        name = "Pasha",
                        email = "pasha@example.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
@SpringBootApplication
public class DiplomicProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomicProjectApplication.class, args);
    }
}

