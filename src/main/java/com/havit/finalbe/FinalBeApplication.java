package com.havit.finalbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinalBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalBeApplication.class, args);
    }

}
