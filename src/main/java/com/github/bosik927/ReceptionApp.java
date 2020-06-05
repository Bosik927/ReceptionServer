package com.github.bosik927;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReceptionApp {

    public static void main(String[] args) {
        SpringApplication.run(ReceptionApp.class, args);
    }
}
