package com.xml.ioc.bean;


import com.xml.ioc.scan.Cat;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class Dog {

	private String name="小黄";

	@Autowired
	private  Student student;


	/**
	 * 使用@Autowired注解进行实列化
	 */
	@Autowired
	public Dog(){
		System.out.println("使用标记了@Autowired注解的无参构造函数进行实列化");
	}
	//@Autowired(required = false)
	public Dog(Student student,Cat cat){
		System.out.println(cat);
		System.out.println(student);
		System.out.println("使用@Autowired无参构造函数实列化");
	}
	/*@Autowired(required = false)
	public Dog(Student student){
		System.out.println(student);
		System.out.println("使用@Autowired有参构造函数实列化");

	}*/
}
