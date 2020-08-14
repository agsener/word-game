package com.beamtech.wordgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WordgameApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordgameApplication.class, args);
	}

}
