package com.main.moeego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "admin")
public class MoeegoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoeegoApplication.class, args);
    }

}
