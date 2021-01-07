package com.ttree.ttree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TtreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtreeApplication.class, args);
    }

}
