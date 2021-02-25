# springMvc的流程

## 1.springMvc的启动

tomcat启动时，根据serlvet3.规范和tomcat的spi技术，会加载类路径META-INF/services/javax.servlet.ServletContainerInitializer中的配置类

springmvc是web包下的SpringServletContainerInitializer类。调用SpringServletContainerInitializer的onStartup方法，

```
@HandlesTypes(WebApplicationInitializer.class)
```

实现了WebApplicationInitializer接口的类作为参数，通过自动类实现AbstractDispatcherServletInitializer，完成无配置springmvc启动。

```
super.onStartup(servletContext);
```

注册一个spring容器的上下文和创建一个ContextLoaderListener的servlet容器的监听器，当servlet容器启动时，调用监听器的初始化方法完成spring容器的启动

**spring容器是根容器**，负载加载除@Controller的所有bean

```
//注册springmvc的上下文，以及注册DispatcherServlet
registerDispatcherServlet(servletContext);
```

注册一个springmvc上下和创建一个DispatcherServlet这个servlet类方法servlet容器中，DispatcherServlet初始化时会进行springMvc容器的启动，

在springmvc启动时主要干了两件事1.设置springmvc的父容器为spring容器 2.为springmvc注册一个ContentextFreshEvet事件，在springMvc容器启动后，会雕调用DispatcherServlet的onRefresh方法，进行，handlerMapping，HandlerAdapter,ViewResolvers的初始化。

如HandlerMapping类型的bean是通过@EnableWebMvc注册进容器的，这里面还会有一系列WebMvcConfigure的钩子方法。

## 2.springMvc的调用流程

 RequestMappingHandlerMapping根据url请求地址与method建立映射关系

### 2.1 RequestMappingHandlerMapping在进行实列化的时候，调用bean的初始化方法afterPropertiesSet()建立@Controller中@RequsstingMapping与method的映射

RequestMappingHandlerMapping在创建的时候会将Interceptor等痛过webMvcConfihurer的钩子方法全部创建好

流程是，遍历所有的bean，判断当前bean上是否有@Controller注解或者@RequestMapping注解

将@RequestMapping注解的参数解析成RequestMappingInfo对象

handlerMethod是对@RequestMapping注解标记的method的包装

```
//mappingLookup容器是建立RequestMappingInfo和与hanlerMehod的映射
this.mappingLookup.put(mapping, handlerMethod);
```

```
//urlLookup容器建立的是url与RequestMappingInfo对象的映射
this.urlLookup.add(url, mapping);
```

### 2.2发送请求

所有http请求都会经过DispatcherServlet的service方法

**这里请求会将request和reponse属性封装成ServletRequestAttributes放入ThreadLocal中**

```
RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
```

**doDispatch()是解决请求调用的核心方法**

通过url在RequestMappingHandlerMapping在实列化时建立好的映射射关系，获取到handlerMethod方法，根据handlerMehotd，找到所有的interceptor，判断是否拦截，封装成HandlerExecutionChain，返回





RequestResponseBodyMethodProcessor：解析json格式的入参和返回参数，里面有一个messageConverters进行request参数的读取和response参数的写入，

messageConverters，可以实现MebMvcConfigurer自定义实现

## 核心

1.RequestMappingHandlerMapping:负责对@Controller下@RequestMapping方法的解析

@EnableMvc注解开启了对RequestMappingHandlerMapping类的实列化，在RequestMappingHandlerMapping创建工程中，spring利用钩子函数（模板设计模式）将WebMvcConfigurer配置的属性赋值到RequestMappingHandlerMapping中，RequestMappingHandlerMapping在初始化时会建立请求路径与方法的映射关系。

SpringMvc执行流程

1.DispatcherServlet解析request，获取到请求路径

2.根据请求路径找到RequestMappingHandlerMapping实列化时urlLookup中的RequestMappingInfo，在根据RequstMappingInfo找到HandlerMethod

3.返回一个HandlerExcuteChain，HandlerExcuteChain包含了拦截器链和HandlerMethod

4.根据HandlerMethod找到对应的HandlerAdapter-->RequestMappingHandlerAdapter，HandlerAdapter包含了参数解析器。**RequestResponseBodyMethodProcessor**对@RequestBody注解标记的参数进行解析

5.调用拦截器的前置方法

6.调用HandlerAdapter的handleInternal方法，执行HanlerMethod的具体逻辑，执行前会根据参数解析器，解析参数(策略模式)，

调用方法后会根据返回结果判断是否需要返回视图

7.**RequestResponseBodyMethodProcessor**解析returnType，判断是否有@ReponseBody，若有mavContainer.setRequestHandled(true);

表明不会创建视图。

83.执行拦截器的中置方法，中置方法会传入ModelAandView对象表明 可以修改mv对象

8.若创建了视图返回的是ModelAndView对象，还需要用视图解析器解析视图，解析成View对象，若是jsp，tomcat进行视图渲染

