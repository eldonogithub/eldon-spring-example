package com.example.demo;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private DemoConfig demoConfig;
	
	@RequestMapping("/")
	public Data home(HttpServletRequest request) {
		log.debug("home() invoked");
		Enumeration<String> initParameterNames = request.getServletContext().getInitParameterNames();
		log.debug("request parameters:");
		while (initParameterNames.hasMoreElements()) {
			String name = (String) initParameterNames.nextElement();
			log.debug("  " + name + "=" + request.getServletContext().getInitParameter(name));
		}
		Data data = new Data(demoConfig.getNumber());
		data.setStatus(HttpStatus.OK);
		
		return data;
	}
	
	@RequestMapping("/boom")
	public void boom() throws Exception {
		throw new Exception();
	}

}
