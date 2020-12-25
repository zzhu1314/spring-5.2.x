package com.xml.ioc.bean;


import com.xml.ioc.scan.Cat;
import org.springframework.beans.factory.annotation.Autowired;

public class Dog {

	private String name="小黄";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public static Student student(){
		return new Student();
	}
	/**
	 * 使用@Autowired注解进行实列化
	 */
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
