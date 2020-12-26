package com.xml.ioc.listener;

//import com.xml.ioc.event.MyEvent;
import com.xml.ioc.event.MyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class MyListener01 implements ApplicationListener<MyEvent> {
	@Override
	@EventListener
	public void onApplicationEvent(MyEvent event) {
		System.out.println("线程名称:"+Thread.currentThread().getName());
		System.out.println("触发自定义MyEvent");
	}
}
