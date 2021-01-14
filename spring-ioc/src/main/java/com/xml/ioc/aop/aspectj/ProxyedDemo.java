package com.xml.ioc.aop.aspectj;

import org.springframework.stereotype.Component;

@Component(value = "proxyedDemo")
public class ProxyedDemo {

	public void test(){
		System.out.println("doSomething....");
	}
}
