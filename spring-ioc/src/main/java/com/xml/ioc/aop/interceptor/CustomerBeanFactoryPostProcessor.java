package com.xml.ioc.aop.interceptor;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义BeanFactoryPostProcessor，提前实列化AnnotationAwareAspectJAutoProxyCreator
 * 设置interceptorNames属性
 */
//@Component
public class CustomerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		AnnotationAwareAspectJAutoProxyCreator creator = beanFactory.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
		creator.setInterceptorNames("customerInterceptor");
	}
}
