package com.xml.ioc.beanmethod;

public class BigCat {
	private Cat cat;

	private YellowTiger yellowTiger;
	public Cat getCat() {
		return cat;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
	}

	public YellowTiger getYellowTiger() {
		return yellowTiger;
	}

	public void setYellowTiger(YellowTiger yellowTiger) {
		this.yellowTiger = yellowTiger;
	}
}
