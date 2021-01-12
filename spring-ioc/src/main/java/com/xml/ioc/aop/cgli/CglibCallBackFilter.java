package com.xml.ioc.aop.cgli;

import org.springframework.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * cglib代理的会回调方法,根据返回的int值去调用具
 */
public class CglibCallBackFilter implements CallbackFilter {
	@Override
	public int accept(Method method) {
		if("doSomething0".equals(method.getName())){
			return 0;
		}else if("doSomething1".equals(method.getName())){
			return 1;
		}else if("doSomething2".equals(method.getName())){
			return 2;
		} else {
			return 3;
		}
	}
}
