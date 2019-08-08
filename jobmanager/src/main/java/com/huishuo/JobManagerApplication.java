package com.huishuo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JobManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobManagerApplication.class, args);
    }
}
