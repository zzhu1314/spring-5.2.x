package com.xml.ioc.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * factoryBean:
 * 1.若想获取bean实列本身需加& getBean("&factoryBeanDemo")
 * 2.getBean("factoryBeanDemo")获取的是getObject()返回的对象
 *
 */
@Component
public class FactoryBeanDemo implements FactoryBean<Shan> {
	@Override
	public Shan getObject() throws Exception {
		return new Shan();
	}

	@Override
	public Class<?> getObjectType() {
		return Shan.class;
	}
}
