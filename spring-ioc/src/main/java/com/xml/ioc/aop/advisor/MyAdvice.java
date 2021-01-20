package com.xml.ioc.aop.advisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;

public class MyAdvice implements MethodInterceptor {

	public final static MyAdvice  INSTANCE = new MyAdvice();
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {

			ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;
			Method method = invocation.getMethod();
			/**
			 * 获取原始方法
			 */
			Method mostSpecificMethod = AopUtils.getMostSpecificMethod(method, reflectiveMethodInvocation.getThis().getClass());
			System.out.println("========before Advice invocation============="+mostSpecificMethod);
			//手动触发火炬传递
			return invocation.proceed();
		} finally {
			System.out.println("============after advice =======");
		}
	}
}
