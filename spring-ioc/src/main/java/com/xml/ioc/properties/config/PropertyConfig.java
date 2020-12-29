package com.xml.ioc.properties.config;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class PropertyConfig implements ResourceLoaderAware {
	private ResourceLoader resourceLoader;
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setLocation(resourceLoader.getResource("application.properties"));
		return propertySourcesPlaceholderConfigurer;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
	}
}
