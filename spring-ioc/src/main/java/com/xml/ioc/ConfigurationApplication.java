package com.xml.ioc;

import com.xml.ioc.beanmethod.BeanMethodConfiguration;
import com.xml.ioc.beanmethod.BigCat;
import com.xml.ioc.beanmethod.Cat;
import com.xml.ioc.beanmethod.YellowTiger;
import com.xml.ioc.conditional.ConditionPropertyBean;
import com.xml.ioc.configuration.ConfigurationClassDemo;
import com.xml.ioc.configurationclass.A;
import com.xml.ioc.configurationclass.B;
//import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationApplication {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationClassDemo.class);
		System.out.println(applicationContext.getBean(A.class));
		/*AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanMethodConfiguration.class);
		BigCat bigCat = applicationContext.getBean(BigCat.class);
		System.out.println("cat:"+bigCat.getCat().hashCode());
		System.out.println(applicationContext.getBean(Cat.class).hashCode());
		System.out.println("tiger:"+bigCat.getYellowTiger().hashCode());
		System.out.println(applicationContext.getBean(YellowTiger.class).hashCode());
*/

	}

	//@Test
	public void test1(){
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanMethodConfiguration.class);
		BigCat bigCat = applicationContext.getBean(BigCat.class);
		System.out.println("cat:"+bigCat.getCat().hashCode());
	}
}
