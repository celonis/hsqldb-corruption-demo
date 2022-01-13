package org.hsqldb.corrupted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbCorruptionApp {

    public static void main(String[] args) {
        SpringApplication.run(DbCorruptionApp.class, args);
    }

}
