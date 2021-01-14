package com.xml.ioc.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * EnableAspectJAutoProxy开启切面功能和 aop自定义标签一下
 * springboot开启的是自动配置AopAutoConfiguration
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(value = {"com.xml.ioc.aop"})
public class ProxyApplication {

}
