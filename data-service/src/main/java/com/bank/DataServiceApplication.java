package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.data.autoconfigure.web.DataWebAutoConfiguration.class})
public class DataServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataServiceApplication.class, args);
    }

}
