package com.xml.ioc.event;

import org.springframework.context.ApplicationEvent;
@SuppressWarnings("serial")
public class MyEvent extends ApplicationEvent {

	private String name;
	public MyEvent(String name ,Object source) {
		super(source);
		this.name =name;
	}
}
