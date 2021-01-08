package com.xml.ioc.configurationclass;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * @Import未解决循环@Import的问题
 */
//@Import(A.class)
public class B {
}
