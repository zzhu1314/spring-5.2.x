package com.xml.ioc.aop.aspectj;

import com.xml.ioc.aop.annotation.MyAnnotation;
import org.springframework.stereotype.Component;

@Component
public class ProxyedDemo implements ProxyedDemoService{

	@Override
	@MyAnnotation
	public void test(){
		System.out.println("test doSomething....");
		System.out.println("=================");
		test02();
	}
	@Override
	public void test02(){
		System.out.println("test02 doSomething.......");
	}
}
