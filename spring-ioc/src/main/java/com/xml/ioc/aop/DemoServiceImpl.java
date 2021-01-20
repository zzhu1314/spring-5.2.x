package com.xml.ioc.aop;

import org.springframework.stereotype.Component;

@Component
public class DemoServiceImpl implements  Demoservice{
	@Override
	public void test() {
		System.out.println("11111");
	}
}
