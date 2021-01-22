package com.xml.ioc.genericity;

import java.lang.reflect.Method;

public class GenericityServiceImpl implements GenericityService<String> {

	/**
	 *由于泛型擦除的原因，编译会为test()生成一个桥接方法
	 * public Object test (Object str){
	 *     return this.test((String)str)
	 * }
	 * @return
	 */
	@Override
	public String test(String str) {
		return "str";
	}

	public static void main(String[] args) {
		Method[] methods = GenericityServiceImpl.class.getDeclaredMethods();
	}
}
