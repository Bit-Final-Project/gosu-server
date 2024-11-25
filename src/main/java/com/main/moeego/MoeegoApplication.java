package com.main.moeego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = {"article", "cancel", "category", "comment", "favorite", "image", "map", "member", "pro", "reserve"})
@EnableJpaRepositories(basePackages = {"member.dao", "admin.repository","article.dao"})
@ComponentScan(basePackages = {"admin","member.service", "article.service", "cancel", "category", "comment", "favorite", "image", "map", "pro", "reserve"})
public class MoeegoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoeegoApplication.class, args);
    }

}
