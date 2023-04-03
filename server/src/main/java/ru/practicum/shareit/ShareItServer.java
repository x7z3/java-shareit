package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.yaml")
public class ShareItServer {
    public static final String X_SHARER_USER_ID_HEADER_NAME = "X-Sharer-User-Id";

    public static void main(String[] args) {
        SpringApplication.run(ShareItServer.class, args);
    }
}
