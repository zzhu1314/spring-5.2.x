package com.xml.ioc.listener;

import com.xml.ioc.event.MyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

@Order(-1)
public class MyListener02 implements ApplicationListener<MyEvent> {
	@Override
	public void onApplicationEvent(MyEvent event) {
		System.out.println("线程名称:"+Thread.currentThread().getName());
		System.out.println("容器启动后手动加入监听器");
	}
}
