package com.xml.ioc.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * EnableAspectJAutoProxy开启切面功能和 aop自定义标签一下
 * springboot开启的是自动配置AopAutoConfiguration
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false,exposeProxy = true)
@ComponentScan(value = {"com.xml.ioc.aop"})
//@Import(ImportAware.class)
public class ProxyApplication {

}
