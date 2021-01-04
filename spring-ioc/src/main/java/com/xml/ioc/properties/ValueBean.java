package com.xml.ioc.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ValueBean implements EnvironmentAware {

	@Value(value = "${zz.name}")
	private  String name;

	public String getName() {
		return name;
	}

	@Override
	public void setEnvironment(Environment environment) {
		System.out.println(name);
		System.out.println(environment.getProperty("zz.name"));
	}
}
