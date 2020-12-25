package com.xml.ioc;

//import com.xml.ioc.event.MyEvent;
import com.xml.ioc.bean.Dog;
import com.xml.ioc.event.MyEvent;
import com.xml.ioc.listener.MyListener02;
import com.xml.ioc.scan.Cat;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.concurrent.Executors;

public class XmlApplication {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		//System.out.println(applicationContext.getBean(Cat.class));

//		//发布事件
////		applicationContext.publishEvent(new MyEvent("jack","myEvent"));
////		System.out.println("===============");
////		SimpleApplicationEventMulticaster eventMulticaster = applicationContext.getBean(SimpleApplicationEventMulticaster.class);
////		//设置多线程执行
////		eventMulticaster.setTaskExecutor(Executors.newSingleThreadExecutor());
////
////		//在容器启动后 将自定义监听器加入到容器中
////		applicationContext.addApplicationListener(new MyListener02());
////		applicationContext.publishEvent(new MyEvent("jack","myEvent"));
		    System.out.println(applicationContext.getBean(Dog.class));

	}

}
