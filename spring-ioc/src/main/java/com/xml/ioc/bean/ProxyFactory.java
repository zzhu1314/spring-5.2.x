package com.xml.ioc.bean;

import org.omg.CORBA.portable.InvokeHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory implements InvocationHandler {
	private Object target;

	public ProxyFactory(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("=================1==============");
		Object invoke = method.invoke(target, args);
		System.out.println("=================2==============");
		return invoke;
	}

	/**
	 * 生成代理对象
	 */
	public Object getProxy() {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
}
