package com.xml.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author:zhuzhou
 * @date: Created in 2022/4/24 15:37
 * @version：1.0.0
 */
@Configuration
@ComponentScan(value = "com.xml.ioc.config")
public class ConfigBean {

	@Bean
	public A a(){
		return new A();
	}

	@Bean
	public B b(){
		a();
		return new B();
	}
}
