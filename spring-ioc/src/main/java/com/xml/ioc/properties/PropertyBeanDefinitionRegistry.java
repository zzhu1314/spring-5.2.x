package com.xml.ioc.properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 自定义设置Environment的参数
 */
@Component
public class PropertyBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

	private Environment environment;
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		StandardEnvironment standardEnvironment = (StandardEnvironment) environment;
		MutablePropertySources propertySources = standardEnvironment.getPropertySources();
		Properties properties = new Properties();
		properties.setProperty("zz.name","zhuchuangjiang");
		propertySources.addLast(new PropertiesPropertySource("customer",properties));
	}

	@Override
	public void setEnvironment(Environment environment) {
         this.environment = environment;
	}
}
