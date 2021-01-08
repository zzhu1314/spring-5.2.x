package com.xml.ioc.conditional;

import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "zz.name")
public class ConditionPropertyBean {
}
