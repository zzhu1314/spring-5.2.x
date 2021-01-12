package com.xml.ioc.aop.cgli;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class DosomethingIntercepter1 implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println(method.getName() + "执行前...");
        //被代理方法
        Object object = methodProxy.invokeSuper(o, objects);
        System.out.println(method.getName() + "执行后...");
        return object;
    }
}
