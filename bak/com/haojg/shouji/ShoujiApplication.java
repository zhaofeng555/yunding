package com.haojg.shouji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ShoujiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoujiApplication.class, args);
	}
}
