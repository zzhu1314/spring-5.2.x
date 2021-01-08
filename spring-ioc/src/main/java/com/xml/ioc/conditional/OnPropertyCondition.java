package com.xml.ioc.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 自定义条件注解实现Condition接口
 */
public class OnPropertyCondition implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Environment environment = context.getEnvironment();
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnProperty.class.getName());
		String property = (String) annotationAttributes.get("value");
		if (!StringUtils.isEmpty(property)) {
			if (environment.getProperty(property) != null) {
				return true;
			}
			return false;
		}
		return true;
	}

}
