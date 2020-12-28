package com.xml.ioc.cyclic.construct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *构造器的循环依赖
 * 在构造器上加@lazy在进行循环依赖的时候 第一次就不会走getBean,而是通过走代理，创建一个代理对象，
 * 只有在真正使用的时候才会走getBean去创建
 * 解决构造器循环依赖问题,再构造器上加@Lazy，且两边的的的构造函数不能同时设计到相关依赖的调用,否则再使用时 两边又同时会走getBean操作
 * 最后生产的还是单列bean
 */
@Service
public class ConCirculationB {

	private ConCirculationA conCirculationA;
	@Lazy
	public ConCirculationB(ConCirculationA conCirculationA){
		System.out.println("构造器conCirculationA的循环依赖");
		this.conCirculationA = conCirculationA;
	}
}
