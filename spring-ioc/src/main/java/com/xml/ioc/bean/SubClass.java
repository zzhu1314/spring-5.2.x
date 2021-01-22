package com.xml.ioc.bean;

import java.lang.reflect.Method;

public class SubClass implements SuperClass {
	@Override
	public String method() {
		return "";
	}

	public static void main(String[] args) throws Exception {
	 /*	System.out.println(SuperClass.class.getName());
 		Method method = Class.forName("com.xml.ioc.bean.SuperClass").getMethod("method", Object.class);
		System.out.println(method.invoke(new SubClass(), "你好"));*/
		// 保存生成的代理类的字节码文件
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		ProxyFactory proxyFactory = new ProxyFactory(new SubClass());
	   SuperClass proxy = (SuperClass) proxyFactory.getProxy();
		proxy.method();
	}
}
