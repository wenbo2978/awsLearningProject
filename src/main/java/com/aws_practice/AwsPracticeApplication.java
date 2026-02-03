package com.aws_practice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aws_practice.mapper")
public class AwsPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsPracticeApplication.class, args);
	}

}
