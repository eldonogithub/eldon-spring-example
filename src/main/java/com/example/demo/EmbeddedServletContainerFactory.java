package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

public class EmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {

	Logger log = LoggerFactory.getLogger(EmbeddedServletContainerFactory.class);

	@Override
	public void addContextCustomizers(TomcatContextCustomizer... tomcatContextCustomizers) {
		log.debug("Adding tomcat context customizers");
		super.addContextCustomizers(tomcatContextCustomizers);
	}

}
