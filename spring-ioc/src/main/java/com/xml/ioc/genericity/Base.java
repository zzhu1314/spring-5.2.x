package com.xml.ioc.genericity;

import com.xml.ioc.configurationclass.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <?>表达式主要是确定类型的范围
 * <? extends T> 整体代表T及T的子类  不具有写的功能，只能接收自己或自己的子类
 * <? super T  > 整体代表T及T的超类  有写的功能 但不能写自己的超类
 */
public class Base {
	public static void main(String[] args) {
		List<Sub1> sub1s = Arrays.asList(new Sub1());
		List<? extends Base> subList = new ArrayList<>();
		subList = sub1s;
		//subList.add(new Sub()); 不具有写的功能
		List<? super Sub> baseList = new ArrayList<>();
		//baseList.add(new Base());// 不能写自己的超类
		baseList.add(new Sub());
		baseList.add(new Sub1());
		System.out.println(Arrays.toString(baseList.toArray()));
	}
}

class Sub extends Base{

}
class Sub1 extends Sub{

}