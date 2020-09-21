package com.logParser.logParser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LogParserApplication {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(LogParserApplication.class, args);
		System.out.println("Started......");
	}

}
