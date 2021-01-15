package com.xml.ioc.aop.interceptor;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 通过获取到BeanDefinition的MutableProperties设置属性值
 * 但这个的BeanDefinitionRegistryPostProcessor必需要在ConfigurationClassPostProcessor后调用，否则
 * 获取AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME失败
 * 若使用注解形式加载IOC就不用关注这个问题，因为CustomerBeanDefinitionRegistryPostProcessor是被ConfigurationClassPostProcessor扫描出来的
 */
//@Component
public class CustomerBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		BeanDefinition beanDefinition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		propertyValues.addPropertyValue("interceptorNames",new String[]{"customerInterceptor","customerInterceptor.CustomerInterceptor1"});

	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}


}
