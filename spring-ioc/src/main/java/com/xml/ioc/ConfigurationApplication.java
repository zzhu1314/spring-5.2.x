package com.xml.ioc;

import com.xml.ioc.bean.Dog;
import com.xml.ioc.beanmethod.BeanMethodConfiguration;
import com.xml.ioc.beanmethod.BigCat;
import com.xml.ioc.beanmethod.Cat;
import com.xml.ioc.beanmethod.YellowTiger;
import com.xml.ioc.conditional.ConditionPropertyBean;
import com.xml.ioc.configuration.ConfigurationClassDemo;
import com.xml.ioc.configurationclass.A;
import com.xml.ioc.configurationclass.B;
//import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * properties属性值解析出来
 * ApplicationContext不是bean 不能通过getBean获取
 * 但可以通过依赖注入和ApplicationContextAware接口实现
 */
public class ConfigurationApplication {
	public static void main(String[] args) throws IOException {
	AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationClassDemo.class);
		System.out.println(applicationContext.getBean(ApplicationContext.class));
		/*System.out.println(applicationContext.getBean(A.class));*/
		/*AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanMethodConfiguration.class);
		BigCat bigCat = applicationContext.getBean(BigCat.class);
		System.out.println("cat:"+bigCat.getCat().hashCode());
		System.out.println(applicationContext.getBean(Cat.class).hashCode());
		System.out.println("tiger:"+bigCat.getYellowTiger().hashCode());
		System.out.println(applicationContext.getBean(YellowTiger.class).hashCode());
*/
		CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
		System.out.println(cachingMetadataReaderFactory.getMetadataReader(Dog.class.getName()).getAnnotationMetadata());
	}

	//@Test
	public void test1(){
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanMethodConfiguration.class);
		BigCat bigCat = applicationContext.getBean(BigCat.class);
		System.out.println("cat:"+bigCat.getCat().hashCode());
	}
}
