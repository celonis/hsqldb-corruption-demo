package org.hsqldb.corrupted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Follow {@link CorruptedDataSavedExample} and {@link DbCorruptionAppTest}
 */
@SpringBootApplication
public class DbCorruptionApp {

    public static void main(String[] args) {
        SpringApplication.run(DbCorruptionApp.class, args);
    }

}
