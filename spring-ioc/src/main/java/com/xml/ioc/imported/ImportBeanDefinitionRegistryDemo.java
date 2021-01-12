package com.xml.ioc.imported;

import com.xml.ioc.configurationclass.A;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

@Component
public class ImportBeanDefinitionRegistryDemo implements ImportBeanDefinitionRegistrar{
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		System.out.println(importingClassMetadata);
		System.out.println("实现ImportBeanDefinitionRegistrar接口，注册BEanDefinition");
	}
}
