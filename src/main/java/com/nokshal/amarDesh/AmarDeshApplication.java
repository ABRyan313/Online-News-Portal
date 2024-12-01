package com.nokshal.amarDesh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AmarDeshApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmarDeshApplication.class, args);
	}

}
