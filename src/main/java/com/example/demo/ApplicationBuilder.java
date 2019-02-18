package com.example.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class ApplicationBuilder extends SpringApplicationBuilder {

	@Override
	public SpringApplicationBuilder web(boolean webEnvironment) {
		return super.web(webEnvironment);
	}

}
