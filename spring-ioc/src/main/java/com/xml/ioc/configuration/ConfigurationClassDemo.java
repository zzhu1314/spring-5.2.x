package com.xml.ioc.configuration;

import com.xml.ioc.bean.Dog;
import com.xml.ioc.configurationclass.A;
import com.xml.ioc.configurationclass.B;
import com.xml.ioc.imported.DeferredImportSelectorDemo;
import com.xml.ioc.imported.ImportBeanDefinitionRegistryDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Configuration()
//@Import({ImportBeanDefinitionRegistryDemo.class,DeferredImportSelectorDemo.class})
//@ComponentScan("com.xml.ioc.configuration")
//@PropertySource(value = "classpath:application.properties")
//@ImportResource("classpath:spring.xml")
public class ConfigurationClassDemo {

	@Bean
	public A a(){
		return new A();
	}

	@Bean
	public B b(){
		return new B();
	}




}
