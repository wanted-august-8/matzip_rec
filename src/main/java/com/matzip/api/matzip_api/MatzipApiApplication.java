package com.matzip.api.matzip_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatzipApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatzipApiApplication.class, args);
    }

}
