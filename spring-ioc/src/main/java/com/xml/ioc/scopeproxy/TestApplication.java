package com.xml.ioc.scopeproxy;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "com.xml.ioc.scopeproxy")
public class TestApplication {


	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
		TestBean bean = applicationContext.getBean(TestBean.class);
		bean.test();
	}
}
