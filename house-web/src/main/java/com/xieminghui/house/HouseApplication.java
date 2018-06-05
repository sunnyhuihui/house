package com.xieminghui.house;

import com.xieminghui.house.mooc1.house.autoconfig.EnableHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@ComponentScan(basePackages = "com.xieminghui.house")
@EnableHttpClient
@EnableAsync
public class HouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseApplication.class, args);
	}
}
