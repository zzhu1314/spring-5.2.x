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