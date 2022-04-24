package com.xml.ioc.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @author:zhuzhou
 * @date: Created in 2022/4/20 15:18
 * @version：1.0.0
 */
@Component
public class MyBeanFactoryProcessor implements BeanFactoryPostProcessor, PriorityOrdered {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("执行[MyBeanFactoryProcessor]的postProcessBeanFactory方法");

	}

	@Override
	public int getOrder() {
		return 0;
	}
}
