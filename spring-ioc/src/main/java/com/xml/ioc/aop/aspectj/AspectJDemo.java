package com.xml.ioc.aop.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 切面
 */
@Component
@Aspect
public class AspectJDemo {

	@Pointcut("execution(public * com.xml.ioc.aop.aspectj.ProxyedDemo.*(..))")
	public void cl(){

	}
	@Pointcut("execution(public * com.xml.ioc.aop.proxy.ProxyDisabledDemo.guojie2(..))")
	public void cl2(){

	}
	@Before(value = "cl()")
	public void before1(){
		System.out.println("proxy before1.......");
	}
	@Before(value = "cl()")
	public void before2(){
		System.out.println("proxy before2.......");
	}
	@Before(value = "cl2()")
	public void before3(){
		System.out.println("proxy before3.......");
	}

	@Around(value = "cl()")
	public void around1(ProceedingJoinPoint joinPoint){
		System.out.println("around1 before.....");
		try {
			joinPoint.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		System.out.println("around1 after.....");
	}

	@Around(value = "cl()")
	public void around2(ProceedingJoinPoint joinPoint){
		System.out.println("around2 before.....");
		try {
			joinPoint.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		System.out.println("around2 after.....");
	}
}
