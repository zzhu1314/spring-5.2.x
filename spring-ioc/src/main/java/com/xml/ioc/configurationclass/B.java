package com.xml.ioc.configurationclass;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Import未解决循环@Import的问题
 */
//@Import(A.class)
@Component
public class B {
}
