package com.xml.ioc.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * <context:property-placeholder/>作用
 * 为容器注入PropertySourcesPlaceholderConfigurer
 * 这是一个BeanFactoryBeanPostProcessor,对BeanDefinition进行值的修改，修改占位符${},优先级最低
 * PropertySourcePlaceholderConfigurer时BeanFactoryPostProcessor会提前实列化，
 * 时实现了EnvironmentAware接口，会为Environment属性赋值
 * <property></>标签的注入是在beanFactory被执行的过程 对MutablePropertyValues的值进行修改
 */
@PropertySource(value = "classpath:application.properties")
public class PropertiesBeanDemo {


	private String name;

	private int age;

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
}
