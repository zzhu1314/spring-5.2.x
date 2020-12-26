package com.xml.ioc.cyclic.construct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *构造器的循环依赖
 */
@Service
@Lazy
public class ConCirculationB {

	@Lazy
	public ConCirculationB(ConCirculationA conCirculationA){
		System.out.println("构造器的循环依赖"+conCirculationA);
	}
}
