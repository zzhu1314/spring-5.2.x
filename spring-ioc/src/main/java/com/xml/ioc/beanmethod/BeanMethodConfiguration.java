package com.xml.ioc.beanmethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
//@Component
public class BeanMethodConfiguration {



	@Bean
	public BigCat bigCat(){
		BigCat bigCat = new BigCat();
		//cat()返回Bean
		Cat cat = this.cat();
		//System.out.println("cat HashCode:"+cat.hashCode());
		bigCat.setCat(cat);
		bigCat.setYellowTiger(this.tiger().getObject());
		return bigCat;
	}
	@Bean
	public Cat cat(){
		return new Cat();
	}
	@Bean
	public Tiger tiger(){
		return new Tiger();
	}
}
