package com.xml.ioc.cyclic.construct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 因为构造器的循环依赖在实列化就会触发
 * 不会走到三级缓存,未触发三级缓存就会在第二次拿的时候就会报错
 */
@Service
public class ConCirculationA {

	private ConCirculationB conCirculationB;
	@Lazy
	public ConCirculationA(ConCirculationB conCirculationB){
		System.out.println("构造器conCirculationB的循环依赖");
		this.conCirculationB = conCirculationB;
		System.out.println(this.conCirculationB);
	}
}
