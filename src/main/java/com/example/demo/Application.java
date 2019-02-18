package com.example.demo;

import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	static Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		log.debug("main() invoked");
		Application application = new Application();
		SpringApplicationBuilder builder = application.createSpringApplicationBuilder();
		application.configure(builder);
		SpringApplication app = builder.build();
		app.addListeners(new PropertiesLogger());
		app.run(args);
	}

	@Override
	// 1
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.debug("onStartup(servletContext) invoked");
		log.debug("servletContext=" + servletContext.getClass().getCanonicalName());
		Enumeration<String> attributeNames = servletContext.getAttributeNames();
		log.debug("Attributes:");
		while (attributeNames.hasMoreElements()) {
			String name = (String) attributeNames.nextElement();
			if ( name.contains("MergedWebXml") ) {
				log.debug("  " + name);
			}
			else {
				log.debug("  " + name + "=" + servletContext.getAttribute(name));
			}
		}
		Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
		log.debug("init parameters:");
		while (initParameterNames.hasMoreElements()) {
			String name = (String) initParameterNames.nextElement();
			log.debug("  " + name);
		}
		super.onStartup(servletContext);
	}

	@Override
	// 2
	protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
		log.debug("creating appliction root");
		return super.createRootApplicationContext(servletContext);
	}

	@Override
	// 3
	protected SpringApplicationBuilder createSpringApplicationBuilder() {
		log.debug("creatingSpringApplication builder");
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		Properties defaultProperties = new Properties();
		defaultProperties.setProperty("spring.config.name", "demo");
		defaultProperties.setProperty("spring.config.location", "classpath:/,classpath:/config/,file:./,file:./config/,file:/home/eldon/external/");
		applicationBuilder.properties(defaultProperties );
		return applicationBuilder;
	}

	@Override
	// 4
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		log.debug("configure(applicationBuilder invoked");
		return application.sources(Application.class);
	}

	@Override
	// 5
	protected WebApplicationContext run(SpringApplication application) {
		log.debug("run(SpringApplication) invoked");

		application.addListeners(new PropertiesLogger());
		return super.run(application);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		log.debug("Initializing Tomcat embedded container factory");
		EmbeddedServletContainerFactory factory = new EmbeddedServletContainerFactory();
	    factory.setPort(9000);
	    factory.setSessionTimeout(10, TimeUnit.MINUTES);
	    return factory;
	}

	@Bean
	public EmbeddedServletContainerCustomizer cookieProcessorCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {

	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	        	log.debug("customizing embedded servlet container");
	            if (container instanceof TomcatEmbeddedServletContainerFactory) {
	            	log.debug("servlet container is tomcat");
	                ((TomcatEmbeddedServletContainerFactory) container)
	                        .addContextCustomizers(new TomcatContextCustomizer() {

	                            @Override
	                            public void customize(Context context) {
	                            	log.debug("customizing context");
	                            }

	                        });
	            }
	            else {
	            	log.debug("servlet container is NOT tomcat");
	            }
	        }

	    };
	}
}
