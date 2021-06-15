package com.xml.ioc.configuration;

import com.xml.ioc.bean.Dog;
import com.xml.ioc.configurationclass.A;
import com.xml.ioc.configurationclass.B;
import com.xml.ioc.imported.DeferredImportSelectorDemo;
import com.xml.ioc.imported.ImportBeanDefinitionRegistryDemo;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Configuration(proxyBeanMethods = false,value = "a")
@Import({ImportBeanDefinitionRegistryDemo.class,DeferredImportSelectorDemo.class})
@ComponentScan("com.xml.ioc.configurationclass")
@PropertySource(value = "classpath:application.properties")
public class ConfigurationClassDemo implements EnvironmentAware {


	@Autowired
	public ConfigurationClassDemo(A a,B b) {

	}

	public ConfigurationClassDemo(Dog dog) {

	}

	Environment environment;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Component
	@Configuration
	public class InnerClass{

	}

}
