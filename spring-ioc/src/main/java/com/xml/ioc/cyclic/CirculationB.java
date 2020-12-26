package com.xml.ioc.cyclic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 循环依赖
 */
@Service
public class CirculationB {
	@Autowired
	private CirculationA circulationA;
	public CirculationB(){
		System.out.println("实例化B");
	}
}
