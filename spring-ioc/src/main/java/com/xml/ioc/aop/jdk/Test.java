package com.xml.ioc.aop.jdk;

import java.lang.reflect.Proxy;

/**
 * proxy对象有Parent属性即InvokingHandler，InvokingHandler又包含目标对象，源码中的h是指InvokingHandler属性
 * 最终由Parent(h)去调的invoke()方法
 */
public class Test {
	public static void main(String[] args) {
		// 保存生成的代理类的字节码文件
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		//People proxy = (People) Proxy.newProxyInstance(Test.class.getClassLoader(), new Class<?>[]{People.class}, new Parent(new XiaoWang()));
		People proxy = (People) new Parent(new XiaoWang()).getProxy();
		proxy.findFriend("s");
	}
}
