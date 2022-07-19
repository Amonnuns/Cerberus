package com.amonnuns.doorman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DoormanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoormanApplication.class, args);
    }
}
