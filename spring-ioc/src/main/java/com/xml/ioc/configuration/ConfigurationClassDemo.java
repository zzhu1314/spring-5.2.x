package com.xml.ioc.configuration;

import com.xml.ioc.bean.Dog;
import com.xml.ioc.configurationclass.A;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Configuration
@Import(A.class)
@ComponentScan("com.xml.ioc.conditional")
@PropertySource(value = "classpath:application.properties")
public class ConfigurationClassDemo implements EnvironmentAware {

//	@Bean(value = "dog1")
//	public Dog dog() {
//		System.out.println("environment=="+environment);
//		return new Dog();
//	}

	Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
