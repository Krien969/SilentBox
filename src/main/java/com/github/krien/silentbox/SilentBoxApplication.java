package com.github.krien.silentbox;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SilentBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SilentBoxApplication.class, args);
    }

}
