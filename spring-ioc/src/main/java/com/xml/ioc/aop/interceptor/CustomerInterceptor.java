package com.xml.ioc.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 *  自定义注册全局Advisor
 *  1.自定义一个MethodInterceptor注册如IOC
 *  2.初始化
 */
@Component
public class CustomerInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("========customerInterceptor============");
		return invocation.proceed();
	}
	@Component
	public static class CustomerInterceptor1 implements MethodInterceptor{
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			System.out.println("========customerInterceptor1============");
			return invocation.proceed();
		}
	}


}
