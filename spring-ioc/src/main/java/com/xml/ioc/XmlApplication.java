package com.xml.ioc;

//import com.xml.ioc.event.MyEvent;
import com.xml.ioc.bean.Dog;
import com.xml.ioc.cyclic.prototype.ProCirculationA;
import com.xml.ioc.cyclic.prototype.ProCirculationB;
import com.xml.ioc.event.MyEvent;
import com.xml.ioc.listener.MyListener02;
import com.xml.ioc.scan.Cat;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * BeanPostProcessor的作用
 * 1.方便扩展,对需要扩展的bean进行自定义扩展，对不需要扩展的bean直接 return bean
 * 符合开闭原则
 */
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
//		Dog dog = applicationContext.getBean(Dog.class);
//		System.out.println(dog);
		//System.out.println(dog.getStudent());
		System.out.println(applicationContext.getBean(ProCirculationA.class));
		System.out.println(applicationContext.getBean(ProCirculationB.class));

	}

}
