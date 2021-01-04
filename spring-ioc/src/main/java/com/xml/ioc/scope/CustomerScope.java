package com.xml.ioc.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * 自定义scope
 * 将bean交给自己定义的容器管理，不交给spring管理
 * SessionScope和RequestScope都是相同的逻辑
 */
public class CustomerScope implements Scope {
	ThreadLocal<Object> threadLocal = new ThreadLocal<>();
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		if(threadLocal.get()!=null){
			return threadLocal.get();
		}
		Object bean = objectFactory.getObject();
		threadLocal.set(bean);
		return bean;
	}

	@Override
	public Object remove(String name) {
		Object scopeBean = threadLocal.get();
		threadLocal.remove();
		return scopeBean;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {

	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}
}
