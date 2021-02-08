package com.xml.ioc.aop.jdk;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理对象依赖被代理对象
 * 代理对象是运行时产生的
 * 1.先通过字符串拼接产生
 * 2.在将字符串通过流的形式 变成.java文件写入磁盘
 * 3.将.java文件编译成.class文件
 * 4.通过类加载器将.class加载入jvm
 * 5.创建代理实列对象，执行对象的目标方法
 *
 *
 *根据接口方法参数 生成代理对象的方法名,可以同时代理多个接口
 */
public class Parent implements InvocationHandler {

	private Object target;

	public Parent(Object people) {
		this.target = people;
	}

	private Animal advised;
	/**
	 * @param proxy  代理对象
	 * @param method 被代理对象的方法
	 * @param args   参数
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		before();
		method.invoke(target, "1");
		after();
		return proxy;
	}

	public void before() {
		System.out.println("before....");
	}

	public void after() {
		System.out.println("after....");
	}

	/**
	 * target为真实的dail对象
	 * @return
	 */
	public Object getProxy(){
		return   Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);

	}


}
