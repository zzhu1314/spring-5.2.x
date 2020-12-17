package com.xml.ioc;

import com.xml.ioc.scan.Cat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlApplication {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		System.out.println(applicationContext.getBean(Cat.class));
	}

}
