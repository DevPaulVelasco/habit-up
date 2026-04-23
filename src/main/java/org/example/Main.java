package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("🚀 ¡HABIT UP LEVANTADO!");
    }

    // ELIMINA EL @Bean de PasswordEncoder de aquí,
    // porque ya lo tienes en SecurityConfig.java
}