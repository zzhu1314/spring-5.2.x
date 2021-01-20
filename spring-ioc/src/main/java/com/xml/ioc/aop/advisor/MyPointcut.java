package com.xml.ioc.aop.advisor;

import com.xml.ioc.aop.annotation.MyAnnotation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

public class MyPointcut implements Pointcut ,MethodMatcher,ClassFilter {
	public final static MyPointcut  INSTANCE  = new MyPointcut();
	@Override
	public ClassFilter getClassFilter() {
		return this;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}

	/**
	 * 对类直接放行，不校验
	 * ClassFilter.TRUE
	 * @param clazz the candidate target class
	 * @return
	 */
	@Override
	public boolean matches(Class<?> clazz) {
		return true;
	}

	/**
	 * jdk动态代理
	 * 这个方法在代理前和代理后都会进去
	 * 代理前进去的方法 时被代理对象的方法
	 * 代理进去的method时接口的方法，不是被代理对象的方法
	 * @param method the candidate method
	 * @param targetClass the target class
	 * @return
	 */
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		/**
		 * 获取原始对象的方法
		 */
		Method mostSpecificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
		if(AnnotatedElementUtils.hasAnnotation(mostSpecificMethod,MyAnnotation.class)){
			boolean annotationPresent = method.isAnnotationPresent(MyAnnotation.class);
			System.out.println("=============Method=====isPresent==============="+annotationPresent);
			boolean annotationPresent1 = mostSpecificMethod.isAnnotationPresent(MyAnnotation.class);
			System.out.println("=============mostSpecificMethod=====isPresent==============="+annotationPresent1);
			return true;
		}

		return false;
	}

	@Override
	public boolean isRuntime() {
		return false;
	}

	@Override
	public boolean matches(Method method, Class<?> targetClass, Object... args) {
		return false;
	}
}
