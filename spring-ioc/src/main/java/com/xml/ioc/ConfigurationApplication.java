package com.xml.ioc;

import com.xml.ioc.conditional.ConditionPropertyBean;
import com.xml.ioc.configuration.ConfigurationClassDemo;
import com.xml.ioc.configurationclass.A;
import com.xml.ioc.configurationclass.B;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationApplication {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationClassDemo.class);
		System.out.println(applicationContext.getBean(ConditionPropertyBean.class));
	}
}
