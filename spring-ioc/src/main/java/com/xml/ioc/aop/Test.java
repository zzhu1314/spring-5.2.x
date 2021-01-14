package com.xml.ioc.aop;

import com.xml.ioc.aop.aspectj.ProxyedDemo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ProxyApplication.class);
		ProxyedDemo bean = applicationContext.getBean(ProxyedDemo.class);
		System.out.println(bean);
		bean.test();
	}
}
