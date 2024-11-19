package com.aladin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.aladin.**.mapper")
public class AladinFromSeoulApplication {

	public static void main(String[] args) {
		SpringApplication.run(AladinFromSeoulApplication.class, args);
	}

}
