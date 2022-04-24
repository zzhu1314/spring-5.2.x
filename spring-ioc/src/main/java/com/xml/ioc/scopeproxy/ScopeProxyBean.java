package com.xml.ioc.scopeproxy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * 当注入多实列bean时，会注入一个代理对象
 * 每次调用的时候 都会通过getBean获取多实例bean
 * 保证每次bean都是多列的
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScopeProxyBean {
	public ScopeProxyBean(){
		System.out.println("shilihua。。。。");
	}
	public void test(){
		System.out.println(111);
	}
}
