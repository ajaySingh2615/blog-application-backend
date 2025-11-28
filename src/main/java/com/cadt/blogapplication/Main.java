package com.cadt.blogapplication;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Force Java to use the modern "Kolkata" zone *before* any DB connections are
        // opened.
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.setProperty("user.timezone", "Asia/Kolkata");

        SpringApplication.run(Main.class, args);
    }

}
