package com.xml.ioc.dosposablebean;

import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PreDestroy;

/**
 * 销毁的bean
 */
public class DisposableBeanDemo implements DisposableBean {
	@Override
	public void destroy() throws Exception {
		System.out.println("实现了DisposableBean销毁bean");
	}

	public void xmlDestoryBean(){
		System.out.println("xml定义的destoryBean销毁bean");
	}

	@PreDestroy
	public void annotationDestory(){
		System.out.println("注解@PreDestory销毁备案");
	}
}
