package com.xml.ioc.genericity;

/**
 * 泛型类
 * 泛型信息只存在于代码编译阶段，在编译后即生成class字节码文件泛型就会被擦除(在进入 JVM 之前，与泛型相关的信息会被擦除掉)，专业术语叫做类型擦除。
 * @param <T>
 */
public class Test<T> {

	/**
	 * test01是普通方法，参数T的类型必须与类上的T一致
	 * @param t
	 */
	public void test01(T t){
		System.out.println(t.getClass().getName());
	}

	/**
	 * 这个是泛型方法，参数T与返回值T的类型相同，但与类上的T无关
	 * <E> E test02 (E t)
	 * @param t
	 * @param <T>
	 * @return
	 */
	public <T> T test02 (T t){
		return t;
	}

	public static void main(String[] args) {
		Test<String> test = new Test<>();
		test.test01("11");
		System.out.println(test.test02(new Integer(1)));
	}
}
