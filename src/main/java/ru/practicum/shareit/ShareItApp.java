package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {
	public static final String X_SHARER_USER_ID_HEADER_NAME = "X-Sharer-User-Id";

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}

}
