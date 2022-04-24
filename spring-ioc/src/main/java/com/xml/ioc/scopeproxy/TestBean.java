package com.xml.ioc.scopeproxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestBean {
	@Autowired
	private ScopeProxyBean scopeProxyBean;
	public void test(){
		scopeProxyBean.test();
		scopeProxyBean.test();
	}
}
