package com.xml.ioc.aop.proxy;

import com.xml.ioc.aop.Demoservice;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 代理失效问题 原因guojie2()不是生成的代理bean调用的，不会走JdkDynamicAopProxy的invoke方法，直接走的this.guojie2()
 * 方法1 自己注入自己，有循环依赖问题，会在三级缓存中生成代理对象
 * 方法2  实现ApplicationContentAware或者BeanFactoryAware接口，直接从缓存中getBean();
 * 设置@EnableAutoAspectJProxy的exposeProxy属性为true，调用JdkDynamicAopProxy的invoke方法时会将当前代理对象放入ThreadLocal 中
 */
@Component
public class ProxyDisabledDemo implements ProxyDisableDemoService, ApplicationContextAware, BeanFactoryAware {

	private ApplicationContext applicationContext;

	private BeanFactory beanFactory;

/*	@Autowired
	@Lazy
	private ProxyDisableDemoService proxyDisableDemoService;*/

	@Autowired
	@Lazy
	private Demoservice demoservice;

	@Override
	public void guojie() {
		System.out.println("guojie....................");
		// 方法1 proxyDisableDemoService.guojie2();
		//方法2
		/*ProxyDisableDemoService bean = applicationContext.getBean(ProxyDisableDemoService.class);
		bean.guojie2();
		ProxyDisableDemoService bean = beanFactory.getBean(ProxyDisableDemoService.class);
		bean.guojie2();*/
		//方法3
		ProxyDisableDemoService proxy = (ProxyDisableDemoService) AopContext.currentProxy();
		proxy.guojie2();
		System.out.println("=======");
		this.guojie2();
	}

	@Override
	public void guojie2() {
		demoservice.test();
		System.out.println("guojie2 proxy....");

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
