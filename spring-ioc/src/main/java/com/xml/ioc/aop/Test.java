package com.xml.ioc.aop;

import com.xml.ioc.aop.aspectj.ProxyedDemo;
import com.xml.ioc.aop.proxy.ProxyDisableDemoService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.comparator.ComparableComparator;

import java.util.ArrayList;
import java.util.Comparator;

public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ProxyApplication.class);
	/*	ProxyedDemo bean = applicationContext.getBean(ProxyedDemo.class);
		System.out.println(bean);
		bean.test();
		ArrayList<Integer> integers = new ArrayList<>();*/
		ProxyDisableDemoService bean = applicationContext.getBean(ProxyDisableDemoService.class);
		bean.guojie();
	}
}
