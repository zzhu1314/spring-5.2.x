package com.xml.ioc.beanmethod;

import org.springframework.beans.factory.FactoryBean;

public class Tiger implements FactoryBean<YellowTiger> {
	@Override
	public YellowTiger getObject()  {
		return new YellowTiger();
	}

	@Override
	public Class<?> getObjectType() {
		return YellowTiger.class;
	}
}
