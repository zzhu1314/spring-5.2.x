package com.xml.ioc;

import com.xml.ioc.configuration.ConfigurationClassDemo;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationApplication {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationClassDemo.class);
	}
}
