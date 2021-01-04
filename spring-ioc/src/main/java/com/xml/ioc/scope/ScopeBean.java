package com.xml.ioc.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("refreshScope")
@Component
public class ScopeBean {
	private String name ="girl";


}
