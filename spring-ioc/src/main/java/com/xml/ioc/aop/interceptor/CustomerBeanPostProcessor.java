package com.xml.ioc.aop.interceptor;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 自定义BeanPostProcessor,时序必须在AnnotationAwareAspectJAutoProxyCreator前注册
 * 否则AnnotationAwareAspectJAutoProxyCreator就不用调用到这个BeanPostprocessor
 * 这里需要注意时序问题
 */
//@Component
public class CustomerBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof AnnotationAwareAspectJAutoProxyCreator){
			  AnnotationAwareAspectJAutoProxyCreator creator = (AnnotationAwareAspectJAutoProxyCreator) bean;
			  creator.setInterceptorNames("customerInterceptor");
		}
		return bean;
	}

	@Override
	public int getOrder() {
		return 45;
	}
}
