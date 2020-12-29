package com.xml.ioc.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueBean {

	@Value(value = "${zz.name}")
	private  String name;

	public String getName() {
		return name;
	}
}
