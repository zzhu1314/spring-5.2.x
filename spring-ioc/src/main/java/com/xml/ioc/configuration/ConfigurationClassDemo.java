package com.xml.ioc.configuration;

import com.xml.ioc.bean.Dog;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ConfigurationClassDemo implements EnvironmentAware {

	@Bean(value = "dog1")
	public Dog dog() {
		System.out.println("environment=="+environment);
		return new Dog();
	}

	Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
