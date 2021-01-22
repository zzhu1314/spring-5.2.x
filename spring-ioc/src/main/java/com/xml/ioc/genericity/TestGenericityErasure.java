package com.xml.ioc.genericity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型擦除
 * 1.泛型信息只存在于代码编译阶段，在编译后即生成class字节码文件泛型就会被擦除(在进入 JVM 之前，与泛型相关的信息会被擦除掉)，专业术语叫做类型擦除。
 * 泛型擦除规则
 * (1)若泛型没有指定具体类型，直接用Object代替作为原始类型
 * (2)若限定类型<T extends ClassA> 则用ClassA代替
 * (3)若有多个限定< T exnteds XClass1 & XClass2 >，使用第一个边界类型XClass1作为原始类型；
 */
public class TestGenericityErasure {
	List<String> list = new ArrayList<>();
	public static void main(String[] args) {
		List<String>  l1 = new ArrayList<>();
		List<Integer>  l2 = new ArrayList<>();
		//在写代码时，无法把一个 String 类型的实例加到 ArrayList<Integer> 中，因为ArrayList<Integer> 和 ArrayList<String> 在编译的时候是完全不同的类型，但是运行结果却是true。这就Java泛型的类型擦除造成的
		System.out.println(l1.getClass()==l2.getClass());
		new TestGenericityErasure().create();

	}

	/**
	 * 泛型约束是在编译时期约束,
	 * 在运行时没有约束,
	 */
	public void create(){
		//list.add(this);  编译时会报错
		try {
			Field field = this.getClass().getDeclaredField("list");
			field.setAccessible(true);
			/*List list = (List) field.get(this);
			list.add(this);
			System.out.println(list.get(0));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
