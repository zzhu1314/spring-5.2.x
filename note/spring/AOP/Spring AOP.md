# Spring AOP 源码解析

## 前言

**AOP 基本概念:**

```java
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

```java
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

```java
//AnnotationAwareAspectJAutoProxyCreator重写了
List<Advisor> candidateAdvisors = findCandidateAdvisors();
```

#### 2.2收集实现了Advisor接口的Advisor实列

```java
//调用父类的findCandidateAdvisors寻找实现了Advisor接口的类型
List<Advisor> advisors = super.findCandidateAdvisors();
```

#### 2.3收集由@Aspect修饰的类,组合成Advisor

```java
//寻找加了@Aspect注解的切面 封装成Advisor类型
if (this.aspectJAdvisorsBuilder != null) {
   advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
}
```

（1）获取所有的bean的BeanName，遍历beanName，根据beanName获取bean类型，判断该bean上是否有@Aspect注解

```java
//若该类型加了@Aspect注解
if (this.advisorFactory.isAspect(beanType))
```

(2)创建实列化切面的工厂，解析切面类，组合生成Advisor（PointCut与Advice方法组合生成Advisor）

```java
//创建出实列化切面的工厂
MetadataAwareAspectInstanceFactory factory =
      new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
//将@Aspect切面构建成Advisor，获取Advisor的核心逻辑
List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
```

(3)获取切面除了加了@PointCut注解的所有方法遍历获取到的方法,根据方法method封装成PointCut，每一个增强方法都会生成Advisor

 获取方法是会对符合增强的方法进行排序，排序的规则是先按注解排序,具体通过ConvertingComparator类进行排序

```java
getAdvisorMethods(aspectClass);
//对增强方法进行排序
methods.sort(METHOD_COMPARATOR);
//创建Advisor就是将增强方法(Advice)与Pointcut组合的类型
Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, 0, aspectName);
```

(4)根据method创建出AspectJExpressionPointcut，这里会过滤掉没有没有Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class注解的方法

```java
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

```java
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

```java
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

```java
//若存在有@Aspect注解标记的切面就会创建一个默认的Advisor --DefaultPointcutAdvisor,作用是用于参数传递
extendAdvisors(eligibleAdvisors);
```

### 5.对所有advisor排序

排序规则：创建的默认Advisor排在第一，根据实现了Advisor接口的Ordered(@Oreder注解，实现了Ordered接口和PriorityOrdered接口)排序

，加了@Aspect注解按前面的方法排序不变

```java
if (!eligibleAdvisors.isEmpty()) {
   eligibleAdvisors = sortAdvisors(eligibleAdvisors);
}
```

## 二、创建代理bean

