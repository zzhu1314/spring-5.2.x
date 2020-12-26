package com.xml.ioc.cyclic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 循环依赖
 */
@Service
public class CirculationA {
	@Autowired
	private CirculationB circulationB;
	public CirculationA(){
		System.out.println("实列化A");
	}
}
