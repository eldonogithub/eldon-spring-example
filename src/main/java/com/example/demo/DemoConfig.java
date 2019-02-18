package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="demo")
//@PropertySource("classpath:application.properties") // This did not work, why not?
//@PropertySource("file:/home/eldon/external/demo.properties") // This did not work, why not?
public class DemoConfig {

	Logger log = LoggerFactory.getLogger(DemoConfig.class);

	private int number;
	
	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}