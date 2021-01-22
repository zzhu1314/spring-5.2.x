# Spring AOP 源码解析

## 前言

**AOP 基本概念:**

```
切面(Aspect) ==> 代表一类功能(日志功能,事务功能,缓存功能)
切面由切入点和增强组成,它既包括横切逻辑的定义，也包括连接点的定义，Spring AOP就是负责实施切面的框架，它将切面所定义的横切逻辑织入到切面所指定的连接点中
在spring AOP中 advisor可理解为一个切面，pointCut连接点,advice增强

连接点(joinPoint)  ==> 一个连接点代表一个被代理的方法
程序执行的某个特定位置：如类开始初始化前、类初始化后、、调用后、方法抛出异常后。一个类或一段程序代码拥有一些具有边界性质的特定点，这些点中的特定点就称为“连接点”。Spring仅支持方法的连接点，即仅能在方法调用前、方法调用后、方法抛出异常时以及方法调用前后这些程序执行点织入增强。连接点由两个信息确定：第一是用方法表示的程序执行点；第二是用相对点表示的方位。

切入点(pointCut)  ==> 多个连接点组成切入点，连接点的集合
每个程序类都拥有多个连接点，如一个拥有两个方法的类，这两个方法都是连接点，即连接点是程序类中客观存在的事物。AOP通过“切点”定位特定的连接点。连接点相当于数据库中的记录，而切点相当于查询条件。切点和连接点不是一对一的关系，一个切点可以匹配多个连接点。在Spring中，切点通过org.springframework.aop.Pointcut接口进行描述，它使用类和方法作为连接点的查询条件，Spring AOP的规则解析引擎负责切点所设定的查询条件，找到对应的连接点。其实确切地说，不能称之为查询连接点，因为连接点是方法执行前、执行后等包括方位信息的具体程序执行点，而切点只定位到某个方法上，所以如果希望定位到具体连接点上，还需要提供方位信息。

增强(advice)  ==>具体增强的代码逻辑
增强是织入到目标类连接点上的一段程序代码，在Spring中，增强除用于描述一段程序代码外，还拥有另一个和连接点相关的信息，这便是执行点的方位。结合执行点方位信息和切点信息，我们就可以找到特定的连接点。
```

**AnnotationAwareAspectJAutoProxyCreator的结构关系图**

![1](.\1.png)

**PointCut:**

​    作用：匹配，拦截

   目的：1.为了生成代理 2.用代理对象调用的时候  3.判断bean是否需要进行代理 ClassFilter ==>类是它拦截,MethodMatcher ==>方法由它进行匹配

  ApectJExpressionPointcut同时实现了ClassFilter和MethodMathcher接口

**Advice:**

  承载了一些增强的逻辑

## 一、Advisor切面的收集

### 1.bean代理的入口

**bean在三个地方有可能产生代理**

(1) 三级缓存处

(2) bean的实例化前 Object bean = resolveBeforeInstantiation(beanName, mbdToUse);

(3)bean的初始化后 wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

### 2.收集所有的Advisor

#### 2.1调用后置处理器postProcessAfterInitialization()方法进入bean的代理

```
@Override
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
   if (bean != null) {
      Object cacheKey = getCacheKey(bean.getClass(), beanName);
      if (this.earlyProxyReferences.remove(cacheKey) != bean) {
         //判断是否需要进行代理
         return wrapIfNecessary(bean, beanName, cacheKey);
      }
   }
   return bean;
}
```

*收集所有的Advisor*

```
//AnnotationAwareAspectJAutoProxyCreator重写了
List<Advisor> candidateAdvisors = findCandidateAdvisors();
```

#### 2.2收集实现了Advisor接口的Advisor实列

```
//调用父类的findCandidateAdvisors寻找实现了Advisor接口的类型
List<Advisor> advisors = super.findCandidateAdvisors();
```

#### 2.3收集由@Aspect修饰的类,组合成Advisor

```
//寻找加了@Aspect注解的切面 封装成Advisor类型
if (this.aspectJAdvisorsBuilder != null) {
   advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
}
```

（1）获取所有的bean的BeanName，遍历beanName，根据beanName获取bean类型，判断该bean上是否有@Aspect注解

```
//若该类型加了@Aspect注解
if (this.advisorFactory.isAspect(beanType))
```

(2)创建实列化切面的工厂，解析切面类，组合生成Advisor（PointCut与Advice方法组合生成Advisor）

```
//创建出实列化切面的工厂
MetadataAwareAspectInstanceFactory factory =
      new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
//将@Aspect切面构建成Advisor，获取Advisor的核心逻辑
List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
```

(3)获取切面除了加了@PointCut注解的所有方法遍历获取到的方法,根据方法method封装成PointCut，每一个增强方法都会生成Advisor

 获取方法是会对符合增强的方法进行排序，排序的规则是先按注解排序,具体通过ConvertingComparator类进行排序

```
getAdvisorMethods(aspectClass);
//对增强方法进行排序
methods.sort(METHOD_COMPARATOR);
//创建Advisor就是将增强方法(Advice)与Pointcut组合的类型
Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, 0, aspectName);
```

(4)根据method创建出AspectJExpressionPointcut，这里会过滤掉没有没有Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class注解的方法

```
validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
//根据增强方法上的表达式封装成Pointcut
AspectJExpressionPointcut expressionPointcut = getPointcut(
      candidateAdviceMethod, aspectInstanceFactory.getAspectMetadata().getAspectClass());
//获取Advice方法上的增强注解
AspectJAnnotation<?> aspectJAnnotation =
				AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
//封装成一个Pointcut
AspectJExpressionPointcut ajexp =
    new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class<?>[0]);
//设置Pointcut的表达式即方法注解上的表达式
ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
```

(5)创建Advice,并将Advice和Pointcut组合封装成Advisor

expressionPointcut：根据method注解上的value封装成的pointcut

candidateAdviceMethod：候选的增强方法

```
 new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod,
      this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
//构造函数的逻辑
//将Pointcut封装到Advisor
this.declaredPointcut = declaredPointcut;
//设置切面名称
this.aspectName = aspectName;
//将真正的point指向前面封装好的Point
this.pointcut = this.declaredPointcut;
this.lazy = false;
//实列化Advice
this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
```

### 3.根据收集到的所有Advisor，获取对当前bean可用的Advisor

```
//主要是由PointCut的MethodMatcher(判断方法上是否需要增强如@Transactional)和ClassFilter判断整个类是否需要增强（根据通知方法(Advice)的表达式）
//只要有一个方法增强就会为这个实列创建代理对象，具体哪个方法需要增强则在JdkDynamicAopProxy的invoke方法时具体判断
List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
	//根据pointCut中的表达式判断candidateAdvisors是否对被代理的bean有效
return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
//判断被代理的clazz是否真的需要Advisor
if (canApply(candidate, clazz, hasIntroductions)) {
    eligibleAdvisors.add(candidate);
}
//pc.getClassFilter() 获取ClassFilter 进行 类的匹配
!pc.getClassFilter().matches(targetClass)
//获取methodMatcher进行方法匹配
MethodMatcher methodMatcher = pc.getMethodMatcher();
```

### 4.创建默认的Advisor

```
//若存在有@Aspect注解标记的切面就会创建一个默认的Advisor --DefaultPointcutAdvisor,advisor的advice是一个ExposeInvocationInterceptor作用是用于参数传递
extendAdvisors(eligibleAdvisors);
```

### 5.对所有advisor排序

排序规则：创建的默认Advisor排在第一，根据实现了Advisor接口的Ordered(@Oreder注解，实现了Ordered接口和PriorityOrdered接口)排序

，加了@Aspect注解按前面的方法排序不变

```
if (!eligibleAdvisors.isEmpty()) {
   eligibleAdvisors = sortAdvisors(eligibleAdvisors);
}
```

## 二、创建代理bean

### 1.代理对象创建的入口

```
/**
 * bean.getClass() 被代理对象的class
 * beanName：被代理对象beanName
 * specificInterceptors:收集到的可用的advisor
 *  new SingletonTargetSource(bean)：targetSource包含了被代理对象
 *  每一个需要被代理的bean都会创建一个新的 ProxyFactory和JdkDynamicAopProxy
 * 最终调用被代理对象的方法会调到JdkDynamicAopProxy的invoke()方法
 */
Object proxy = createProxy(
      bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
```

### 2.创建代理工厂，设置proxyFactory的属性

```
/**
 * 创建代理工厂
 
 */
ProxyFactory proxyFactory = new ProxyFactory();
/**
 * 拷贝AnnotationAwareAspectJAutoCreator的proxyTargetClass 和 exposeProxy 属性
 * proxyTargetClass：是否开启CGLIB代理 默认false
 * exposeProxy是否将代理对象放入ThreadLocal中 默认false
 */
proxyFactory.copyFrom(this);
```

### 3.收集全局拦截器和前面封装好的候选Advisor

```
/**
 * 根据前面收集到可用的Advisor和自定义的全局Advisor组合成新的Advisor
 */
Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
/**
* 收集全局Advisor
* 需要自定义Advisor
*/
Advisor[] commonInterceptors = resolveInterceptorNames();
/**
* interceptorNames是AnnotationAwareAspectJAutoProxyCreator中的属性
* 需要开发者自定义创建
* 全局拦截的DefaultPointcutAdvisor(methodInterceptor)
*/
for (String beanName : this.interceptorNames) {
if (cbf == null || !cbf.isCurrentlyInCreation(beanName)) {
Assert.state(bf != null, "BeanFactory required for resolving interceptor names");
Object next = bf.getBean(beanName);
advisors.add(this.advisorAdapterRegistry.wrap(next));
}
}
```

### 4.封装属性到到ProxyFactory

```
//advisor包装到proxyFactory中
proxyFactory.addAdvisors(advisors);
//targetSource包装到proxyFactory中 targetSource中包装了被代理bean
proxyFactory.setTargetSource(targetSource);
```

### 5.创建代理对象

```
 proxyFactory.getProxy(getProxyClassLoader());
 createAopProxy().getProxy(classLoader);
 //创建JdkDynamicAopProxy
//设置config.isProxyTargetClass()为true并不一定为CGLIB代理
  if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
       return new JdkDynamicAopProxy(config);
 }
 

 //jdk动态代理创建代理对象
 JdkDynamicAopProxy.getProxy(){
  return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
 }

```

## 三、被代理方法的调用

**接下来以JDK的动态代理为列,调用被代理方法最终回调用到dkDynamicAopProxy的invoke()方法**

### 1.获取拦截链,用于被代理方法的增强

 
* 	根据方法获取方法的拦截器链 advised 就是proxyFactory，
* 	这里用Object类型接收是因为：
* 	1.若需要进行方法的参数匹配(MethodMatcher.isRunTime==true) 则返回InterceptorAndDynamicMethodMatcher类型,在**方法反射调用时 再次进行参数匹配**
* 	2.若直接进行方法匹配,无需进行参数匹配则返回MethodInterceptor
*   3.若是@Before,@AfterReturning,@AfterThrowing**这类advice实现了advice接口的增强逻辑却未实现MethodInterceptor**，会进行适配成一个MethodInterceptor

```

List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
```

 如果返回的chain为空，则表明被代理方法无需增强，直接反射调用

### 2.创建方法的增强器

```
/**
 * JDK动态代理 创建一个方法的增强器
 *proxy:生成的代理对象
 * target：被代理对象
 * method：接口方法不被代理对象的方法，通过这个method无法获取到被代理方法上的注解 需通过AopUtils.getMostSpecificMethod(method,targetClass)
 * args：参数
 * targetClass：被代理对象的Class类型
 * chain：拦截链，增强链
 */
MethodInvocation invocation =
      new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
```

### 3.对被代理方法拦截链调用，进行方法增强

```
invocation.proceed();
```

**这个方法对interceptor进行传递调用**

**interceptor的顺序根据前面advisor的顺序一致**

<u>**优先级排序：**</u>

（1）若有@Aspect切面，系统会扩展一个DefaultPointCutAdvisor(ExposeInvocationInterceptor)

ExposeInvocationInterceptor作为一个advice,会将***invocation***放入ThreadLocal中，这个的**优先级最高**

（2）直接实现了advisor接口的bean，这类是自定义Advisor，用于扩展。

（3）由@Aspect注解构成的切面，如@Before，@Around等



**扩展:解决在方法内调方法代理失效问题**

/**
 * 代理失效原因:**普通方法的调用(proxy.test())会进JdkDynamic.invoke()的方法,但不会走增强，直接反射调用该方法，方法内部的方法调用直接走的this.zengqiang(),这个this是被代理的对象(目标对象target),而非代理对象，所以第二次不会进JdkDynamic.invoke()方法，无法进行增强**
 **解决方法：应用代理对象调 zengqiang() 即proxy.zengqiang();**
 * 方法1 自己注入自己，有循环依赖问题，会在三级缓存中生成代理对象
 * 方法2  实现ApplicationContentAware或者BeanFactoryAware接口，直接从缓存中getBean();
 * 3.设置@EnableAutoAspectJProxy的exposeProxy属性为true，调用JdkDynamicAopProxy的invoke方法时会将当前代理对象放入ThreadLocal 中
 */

```
public void test(){
  zengqiang();//这个是需要增强的方法
}
```

**Pointcut中MethodMatcher的match方法调用**

  match方法会被调用两次，

  第一次调用是在代理前，若有一个方法匹配则表明advisor匹配这个类，会为这个类创建代理，

 第二次调用是在代理生成后，调用方法时，会对每个方法进行匹配，若匹配成功，会将advice封装成interceptor，进行拦截器的链式调用。

 这两次调用的入参method不是同一个，第一次method是被代理类的方法，第二次method是接口的方法。

```
public boolean matches(Method method, Class<?> targetClass) 
```