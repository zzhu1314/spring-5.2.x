package com.xml.ioc.aop.cgli;

public class Test {
	public static void main(String[] args) {
		UserServiceImpl proxy = (UserServiceImpl) CglibBeanFactory.getInstance();
		proxy.doSomething2("11");
	}
}
