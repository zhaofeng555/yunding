package com.haojg.yunding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class YunDingApplication {

	public static void main(String[] args) {
		SpringApplication.run(YunDingApplication.class, args);
	}
}
