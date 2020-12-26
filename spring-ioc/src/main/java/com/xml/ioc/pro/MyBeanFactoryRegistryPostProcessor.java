package com.xml.ioc.pro;

import com.xml.ioc.annotation.MyService;
import com.xml.ioc.bean.Dog;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 自定退
 */
//@Component
public class MyBeanFactoryRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
	/**
	 * 对BeanDefinition的增删改查
	 * @param registry the bean definition registry used by the application context
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));
		//自定义BeanDefinition
		GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
		genericBeanDefinition.setBeanClass(Dog.class);
		//MutablePropertyValues设置bean的属性
		MutablePropertyValues propertyValues = genericBeanDefinition.getPropertyValues();
		propertyValues.add("name","zhizhi");
		registry.registerBeanDefinition("dog",genericBeanDefinition);

		registry.removeBeanDefinition("customerScanner");

        //自定义扫描
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
		//自定义注解
		scanner.addIncludeFilter(new AnnotationTypeFilter(MyService.class));
		scanner.scan("com.xml.ioc.scan");


	}

	/**
	 * 自定义beanFactory的属性
	 * @param beanFactory the bean factory used by the application context
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		//是否允许循环依赖
		defaultListableBeanFactory.setAllowCircularReferences(true);

	}
}
