package com.xml.ioc.aop.cgli;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;

public class CglibBeanFactory {

	public static Object getInstance(){
		//创建增强器
		Enhancer enhancer = new Enhancer();
		//设置被代理对象
		enhancer.setSuperclass(UserServiceImpl.class);
		//cglib的回调函数,根据返回的int值去调用具体的intercepter(CallBack)
		CallbackFilter callbackFilter = new CglibCallBackFilter();
		enhancer.setCallbackFilter(callbackFilter);
		//这个NoOp表示no operator，即什么操作也不做，代理类直接调用被代理的方法不进行拦截。
		Callback noop = NoOp.INSTANCE;
		Callback callback1 = new DosomethingIntercepter1();
		Callback callback2 = new DosomethingIntercepter2();
		Callback callback3 = new DosomethingIntercepter3();
		enhancer.setCallbacks(new Callback[]{callback1,callback2,callback3,noop});
		return enhancer.create();
 	}
}
