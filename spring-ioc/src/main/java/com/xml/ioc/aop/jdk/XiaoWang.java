package com.xml.ioc.aop.jdk;

public class XiaoWang implements People ,Animal {
	@Override
	public void findFriend(String s) {
		System.out.println("find friend!!!!");
	}

	@Override
	public void eat() {

	}
}
