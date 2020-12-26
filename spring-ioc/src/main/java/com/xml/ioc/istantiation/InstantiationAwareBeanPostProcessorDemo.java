package com.xml.ioc.istantiation;

import com.xml.ioc.bean.Dog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * postProcessAfterInstantiation若返回false则不进行依赖注入
 */
//@Component
public class InstantiationAwareBeanPostProcessorDemo implements InstantiationAwareBeanPostProcessor {
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		if(bean instanceof Dog){
			return false;
		}
        return true;
	}
}
