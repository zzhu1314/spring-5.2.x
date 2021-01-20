package com.xml.ioc.aop.advisor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 自定义advisor
 */
@Component
public class MyAdvisor extends AbstractPointcutAdvisor implements Serializable {
	private static final long serialVersionUID = -219409465008115215L;
	private Pointcut pointcut = MyPointcut.INSTANCE;

	private Advice advice = MyAdvice.INSTANCE;

	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

	@Override
	public Advice getAdvice() {
		return advice;
	}
}
