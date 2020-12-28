package com.xml.ioc.listener;

import com.xml.ioc.event.MyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 使用@EventListener将监听器加入到派发器的listeners集合中
 * EventListenerMethodProcessor处理器来处理
 */
@Component
public class MyListener03 {

	@EventListener
	public void publishEvent(MyEvent event){
		System.out.println("@EventListener触发事件"+event);
	}
}
