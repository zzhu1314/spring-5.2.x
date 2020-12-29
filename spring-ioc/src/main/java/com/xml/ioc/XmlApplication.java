package com.xml.ioc;

//import com.xml.ioc.event.MyEvent;
import com.xml.ioc.bean.Dog;
import com.xml.ioc.cyclic.prototype.ProCirculationA;
import com.xml.ioc.cyclic.prototype.ProCirculationB;
import com.xml.ioc.dosposablebean.DisposableBeanDemo;
import com.xml.ioc.event.MyEvent;
import com.xml.ioc.listener.MyListener02;
import com.xml.ioc.properties.PropertiesBeanDemo;
import com.xml.ioc.properties.ValueBean;
import com.xml.ioc.scan.Cat;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
		 /*//原型循环依赖
		System.out.println(applicationContext.getBean(ProCirculationA.class));
		System.out.println(applicationContext.getBean(ProCirculationB.class));*/
//		System.out.println(System.getProperties());
//		System.out.println(System.getenv());

		/**销毁bean**/
		/*DisposableBeanDemo disposableBeanDemo = applicationContext.getBean(DisposableBeanDemo.class);
		//xml定义的destory-method方法无法被调用
		applicationContext.getBeanFactory().destroyBean(disposableBeanDemo);
		//所有销毁方法都会被调用，销毁逻辑都被放在了DisposableBeans中
		applicationContext.close();
*/
	/*	*//**properties属性的注入**//*
		PropertiesBeanDemo propertiesBeanDemo = applicationContext.getBean(PropertiesBeanDemo.class);
		System.out.println(propertiesBeanDemo.getName()+"-------"+propertiesBeanDemo.getAge());*/
		/**@value属性注入**/
		ValueBean valueBean = applicationContext.getBean(ValueBean.class);
		System.out.println(valueBean.getName());
	}


	public static void test(){
		ArrayList<Integer> source = new ArrayList<>();
		source.add(1);
		source.add(3);
		source.add(2);
		source.add(9);
		source.add(7);
		System.out.println(source);
		//升序
		Integer[] target1 = source.stream().sorted(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
		}).toArray(Integer[]::new);
		System.out.println(Arrays.toString(target1));
		//降序
		Integer[] target2 = source.stream().sorted(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2-o1;
			}
		}).toArray(Integer[]::new);
		System.out.println(Arrays.toString(target2));
	}

}
