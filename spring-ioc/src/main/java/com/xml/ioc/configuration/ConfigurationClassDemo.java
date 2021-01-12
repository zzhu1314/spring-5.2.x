package com.xml.ioc.configuration;

import com.xml.ioc.bean.Dog;
import com.xml.ioc.configurationclass.A;
import com.xml.ioc.configurationclass.B;
import com.xml.ioc.imported.DeferredImportSelectorDemo;
import com.xml.ioc.imported.ImportBeanDefinitionRegistryDemo;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Configuration
@Import({ImportBeanDefinitionRegistryDemo.class})
//@ComponentScan("com.xml.ioc.conditional")
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
/*	@Bean
	public A a(){
		return new A();
	}
	@Bean
	public B b(){
		return new B();
	}*/
	@Component
	@Configuration
	public class InnerClass{

	}

}
