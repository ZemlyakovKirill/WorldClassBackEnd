package ru.itmo.worldclassbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Locale;

@EnableWebMvc
@SpringBootApplication
public class WorldClassBackEndApplication {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru"));
        SpringApplication.run(WorldClassBackEndApplication.class, args);
    }
}
