package com.xml.ioc.aop;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * 只有import进来的类才能获取到importMetadata
 */
@Component
public class ImportAware implements org.springframework.context.annotation.ImportAware {
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		System.out.println(importMetadata);
	}
}
