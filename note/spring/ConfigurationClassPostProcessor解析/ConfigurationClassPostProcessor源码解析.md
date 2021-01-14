# ConfigurationClassPostProcessor源码解析

**ConfigurationClassPostProcessor类名下面全部用ccpp代替**

```
private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
```

**metadataReaderFactory用于读取metadata元数据信息**

**AnnotationMetada：类的元信息，包含类的注解信息，类的方法注解信息**

## 一、类图

![1](.\1.png)

1.ccpp实现了ProotityOrder接口在getOrder()方法中的返回值Ordered.LOWEST_PRECEDENCE为一个Integer.MAX_VALUE,所以在执行BeanDefintionRegistryPostProcessor中是所有实现了ProotityOrder接口最后执行的。

2.postProcessBeanDefinitionRegistry()方法主要是负责扫描@Configuration，@Component，@Import，@ImportResource，@Bean，@ComponentSca这些beanDeefiniton的扫描和注册，以及@PropertySource配置文件的处理。

3.postProcessBeanFactory()方法主要是对加了@Configuration类的CGLI的动态代理。

## 二、postProcessBeanDefinitionRegistry()方法解析

 

![2](.\2.png)

1. 遍历当前容器的所有BeanDefinitionNames。

2. 判断该beanDefinition是否进入候选容器，若该beanDefinition有@Configuratuon注解修饰，设置该属性为全匹配

   ```java
   Map<String, Object> config = metadata.getAnnotationAttributes(Configuration.class.getName());
   //若注解信息里面有Configuration注解则为全部配  config.get("proxyBeanMethods")获取Configuration的属性值
   if (config != null && !Boolean.FALSE.equals(config.get("proxyBeanMethods"))) {
      beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_FULL);
   }
   ```

3.若该BeanDefinition由@Component，@ComponentScan，@Import,@ImportResource修饰或者有@Bean修饰的方法则设置该属性为部分匹配

```
if (config != null || isConfigurationCandidate(metadata)) {
   beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_LITE);
}
```

4.创建ConfigurationClassParser解析器，metadataReaderFactory	

```java
ConfigurationClassParser parser = new ConfigurationClassParser(
      this.metadataReaderFactory, this.problemReporter, this.environment,
      this.resourceLoader, this.componentScanBeanNameGenerator, registry);
```

5.parse() 对ConfigurationClass进行解析将需要处理的类(@Import,内部类)放入ConfigurationClasses容器中。

## 三、 processConfigurationClass()方法解析

首先会根据当期BeanDefinition的metadata信息构建成一个ConfigurationClass类，这个方法会进行递归解析

```java
processConfigurationClass(new ConfigurationClass(metadata, beanName), DEFAULT_EXCLUSION_FILTER);
```

ConfigurationClass有4个容器：

```java
//被谁导入，存储内部类的外部类和加了@Import注解的类
private final Set<ConfigurationClass> importedBy = new LinkedHashSet<>(1);
```

```java
//保存@Bean的方法
private final Set<BeanMethod> beanMethods = new LinkedHashSet<>();
```

```java
//@ImportResource信息
private final Map<String, Class<? extends BeanDefinitionReader>> importedResources =
      new LinkedHashMap<>();
```

```java
//保存实现了ImportBeanDefinitionRegistry接口的类
private final Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> importBeanDefinitionRegistrars =
      new LinkedHashMap<>();
```

### 1.@Conditional注解解析

​    判断当前这个Bean是否需要跳过注册,若该类由@Conditional（A.Class）修饰，则需要进行校验A.Class必须实现Condition接口，若返回true则无需跳过

```java
if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
   return;
}
```

```java
//调用Condition类的matches方法
if ((requiredPhase == null || requiredPhase == phase) && !condition.matches(this.context, metadata)) {
   return true;
}
```

 根据configurationClass创建出sourceClass,**configurationClass和sourceClass都包含metadata信息**

```java
SourceClass sourceClass = asSourceClass(configClass, filter);
```

doProcessConfigurationClass（）核心方法解析

### 2.内部类解析

  判断当前类是否由@Component注解修饰，若有则需要进行内部类递归校验

```java
//递归处理内部类
if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
   // Recursively process any member (nested) classes first
   //configClass
   processMemberClasses(configClass, sourceClass, filter);
}
```

获取当前类的全部内部类，若不为空，则遍历当前所有内部类，判断这些内部类是否需要处理，判断逻辑与（二、3）一致，并加入候选容器。

遍历内部类的候选容器，若不为空则将当前内部类封装成ConfigurationClass进行递归解析，这里会将内部类的相对外部类(父类)加入到importedBy容器中，被谁导入的，后面通过判断这个容器是否为空，确定这个类是否需要封装成BeanDefinition。

```java
//递归调用处理内部类，candidate.asConfigClass将内部类相对的外部类放入ImportedBy集合中,并将当前内部类封装成ConfigurationClass
processConfigurationClass(candidate.asConfigClass(configClass), filter);
```

### 3.@PropertySource注解解析

(1)首先获取这个类的PropertySources和PropertySource注解的信息，遍历注解的信息进行解析

```java
if (this.environment instanceof ConfigurableEnvironment) {
   //处理@PropertySource注解
   processPropertySource(propertySource);
}
```

(2)首先获取propertySource的属性name,若为空则name=null

```java
//获取@PropertySource注解的属性name,这个name可能重复 后面将相同name合并成一个ResourcePropertySource
String name = propertySource.getString("name");
```

(3)获取propertySource的属性value，value值为配置文件的路径

```java
//获取@PropertySource注解的属性值value,即配置文件的路径
String[] locations = propertySource.getStringArray("value");
```

(4)遍历所有的路径，将配置文件封装成Resource对象后，创建出一个PropertySource<?>对象，实际上是一个ResourcePropertySource对象继承自PropertiesPropertySource。

```java
factory.createPropertySource(name, new EncodedResource(resource, encoding))
```

(5)将ResourcePropertySource添加到Environment(StandardEnvironment)中的MutablePropertySources对象中，

   根据当前name，MutablePropertySources中是否有重名的PropertySource，若存在则合并成一个CompositePropertySource

```java
//就是把两个name相同的propertySource放在一个Set集合中
CompositePropertySource composite = new CompositePropertySource(name);
composite.addPropertySource(newSource);
composite.addPropertySource(existing);
//替换掉原来的PropertySource
propertySources.replace(name, composite);
```

若不存在，则直接添加到MutablePropertySources中

```java
if (this.propertySourceNames.isEmpty()) {
   //如果没有直接加入
   propertySources.addLast(propertySource);
}
```

### 4.@ComponentScan注解解析

(1) 获取类上@ComponentScan和@ComponentScans的注解信息

```java
Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
      sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
```

(2) 调用ComponentScanParser解析器解析ComponentScan的信息，返回BeanDefinition

```
Set<BeanDefinitionHolder> scannedBeanDefinitions =
      this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
```

(3) 在ComponentScan的解析器中，创建一个包扫描器ClassPathBeanDefinitionScanner进行包扫描,包扫描器有默认的过滤器TypeFiler

```java
//创建扫描器
ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
      componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
//扫描
return scanner.doScan(StringUtils.toStringArray(basePackages));
```

(4) 对包扫描生成的BeanDefinition进行递归解析，一样判断是否有@Componet，@ComponentScan，内部类，@Import，@ImportResource，@Bean等注解

```java
if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
   //又会对通过包扫描创建的BeanDefiniton进行递归解析
   parse(bdCand.getBeanClassName(), holder.getBeanName());
}
```

### 5.@Import注解解析

(1)递归收集类上的注解是否有@Import注解，注解类上的注解是否有@Import

```java
getImports(sourceClass)
collectImports(sourceClass, imports, visited);
  //visited集合是已经添加过的@Import类
		if (visited.add(sourceClass)) {
			for (SourceClass annotation : sourceClass.getAnnotations()) {
				String annName = annotation.getMetadata().getClassName();
				if (!annName.equals(Import.class.getName())) {
					//若其他注解除了(Import)内部含有@Import注解也递归收集 类如@EnableAutoConfiguration里面的@Import注解
					collectImports(annotation, imports, visited);
				}
			}
			//收集@Import注解
			imports.addAll(sourceClass.getAnnotationAttributes(Import.class.getName(), "value"));
		}
```

(2)处理@Import注解

```java
processImports(configClass, sourceClass, getImports(sourceClass), filter, true);
```

(2.1)首先判断Import进来的类是否实现了ImportSelector接口，若实现则通过反射创建出Selector对象，这个类是不会进行实列化的

```java
//反射创建出selector
ImportSelector selector = ParserStrategyUtils.instantiateClass(candidateClass, ImportSelector.class,
      this.environment, this.resourceLoader, this.registry);
```

判断selector是否为DeferredImportSelector的实现，若是则对selector进行处理，**不会直接调用selectImports()方法**

```java
//若这个类实现了DeferredImportSelector，就不执行selectImports方法
if (selector instanceof DeferredImportSelector) {
    this.deferredImportSelectorHandler.handle(configClass, (DeferredImportSelector) selector);
}
```

DeferredImportSelector实现了ImportSelector，且内部有一个子接口Group,

若selector冲写了DeferredImportSelector的getImportGroup()方法且不返回空，即代表返回类实现了了Group接口，则会调用Group接口的Process()方法

实现selectImports()返回结果的注册逻辑，若没有重写getImportGroup()即返回的Group对象为空，则会进行默认的方法调用最终也会调到selectImports()方法



若selector不为DeferredImportSelector的子类，**则直接执行selectImports方法**

```java
String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
//将返回的类封装成SourceClass
Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames, exclusionFilter);
//递归处理
processImports(configClass, currentSourceClass, importSourceClasses, exclusionFilter, false);
```

(2.2)若import进来的类是实现了ImportBeanDefinitionRegistrar接口，则将该类加入到importBeanDefinitionRegistrars容器中，currentSourceClass指的是导入

ImportBeanDefinitionRegistrar类的类（父类），并非本类

```
configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
```

(2.3)如果这两个接口都没实现，就会把该类的import类(谁导入它的)加入到ImportedBy容器中，并递归判断这个类是否为***ccpp***需要处理的类

```java
//candidate为子，configClass为父
processConfigurationClass(candidate.asConfigClass(configClass), exclusionFilter);
```

### 6.@ImportResource注解解析

将@ImportResource注解的信息加入到importedResources容器中

```java
configClass.addImportedResource(resolvedResource, readerClass);
```

### 7.@Bean注解解析

解析configurationClass类中是否有@Bean修饰的方法，若有通过AnnotationMetadata信息获取MethodMetadata,并封装成BeanMethod，加入到beanMethods容器中

```java
//解析加了@Bean注解的方法，并封装成BeanMethod属性
// 后续实际上就是设置BeanDefinition的factoryMethodBean和factoryMethod属性
Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
for (MethodMetadata methodMetadata : beanMethods) {
   configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
}
```

**最后把处理完的ConfigurationClass加入到configurationClasses容器中，供后面注册成BeanDefinition使用**

```java
this.configurationClasses.put(configClass, configClass);
```

## 四、将configurationClasses容器的类注册成BeanDefinition

首先获取configurationClasses容器中的所有ConfigurationClass，并执行注册逻辑

```java
//将configClasses注册成BeanDefinition
this.reader.loadBeanDefinitions(configClasses);
```

### 1.importedBy容器的解析

```java
 //判断configClass的ImportedBy容器是否为空，若不为空则表示当前类需要注册成BeanDefinition
//内部类和由@Import导入的普通类和@Import导入的ImportSelector类返回的类（递归解析成普通类）
if (configClass.isImported()) {
   registerBeanDefinitionForImportedConfigurationClass(configClass);
}
```

### 2.beanMethods容器的解析

  其实就是创建一个BeanDefinition，并设置BeanDefinition的属性，若为static方法则设置BeanClass和factoryMethodName，

  若为非静态方法设置factoryMethodName和FactoryBeanName，通过工厂方法进行实列化

```java
if (metadata.isStatic()) {
   // static @Bean method
   if (configClass.getMetadata() instanceof StandardAnnotationMetadata) {
      //静态工厂
      beanDef.setBeanClass(((StandardAnnotationMetadata) configClass.getMetadata()).getIntrospectedClass());
   }
   else {
      beanDef.setBeanClassName(configClass.getMetadata().getClassName());
   }
   beanDef.setUniqueFactoryMethodName(methodName);
}
else {
   // instance @Bean method
   //设置工厂bean的名字
   beanDef.setFactoryBeanName(configClass.getBeanName());
   //设置工厂方法的名字 ，后面bean的实例化会用到
   beanDef.setUniqueFactoryMethodName(methodName);
}
```

### 3.importedResource容器的解析

```java
  //处理@ImportResource，通过XmlBeanDefinitionReader注册
loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
```

### 4. importBeanDefinitionRegistrars容器的解析

```java
//调用实现了importBeanDefinitionRegistry接口的方法
loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
```

## 五. postProcessBeanFactory()方法解析

**该方法主要用于增强@Configuratuon注解修饰的类,为了防止破坏@Bean方法中创建实列的单列性,增强方式为CGLIB代理**

### 1.enhanceConfigurationClasses()方法解析

```java
//为加了@Configuration的类生成代理
enhanceConfigurationClasses(beanFactory);
```

(1) 遍历所有的BeanDefinitionNames,通过name获取到BeanDefinition，获取BeanDefinition的Attribute 属性ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE的值，在执行postProcessBeanDefinitionRegistry()方法时为这个属性赋了值，

若属性为全匹配，放入候选容器中

```java
if (ConfigurationClassUtils.CONFIGURATION_CLASS_FULL.equals(configClassAttr)) {
    //属性若为full放入候选容器中后面处理
 configBeanDefs.put(beanName, (AbstractBeanDefinition) beanDef);
}
```

### 2.创建代理对象

(1)调用创建增强器的工具类,创建代理对象

```java
//创建增强器的工具类
ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
//创建代理类对象
Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
//创建出代理对象|
Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
```

(2)newEnhancer(configClass, classLoader)会创建一个CGLIB代理的增强器

```java
//会根据CallBackFilter返回的int值调用CALLBACKS中的方法，NoOp.INSTANCE不拦截
private static final Callback[] CALLBACKS = new Callback[] {
    new BeanMethodInterceptor(),
    new BeanFactoryAwareMethodInterceptor(),
    NoOp.INSTANCE
};
/**
	 * CALLBACK_FILTER 是回调过滤器
	 * CALLBACKS是回调的具体方法
	 */
private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);
//创建增强器
private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
   Enhancer enhancer = new Enhancer();
   //被代理的对象
   enhancer.setSuperclass(configSuperClass);
   enhancer.setInterfaces(new Class<?>[] {EnhancedConfiguration.class});
   enhancer.setUseFactory(false);
   enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
   enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));
   //设置回调的过滤器
   enhancer.setCallbackFilter(CALLBACK_FILTER);
   enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
   return enhancer;
}
```

通过CALLBACK_FILTER的accpet()方法返回的具体值与CALLBACKS数组的下标做匹配,返回那个类就由哪个类做拦截

```java
@Override
public int accept(Method method) {
   for (int i = 0; i < this.callbacks.length; i++) {
      Callback callback = this.callbacks[i];
      if (!(callback instanceof ConditionalCallback) || ((ConditionalCallback) callback).isMatch(method)) {
         return i;
      }
   }
   throw new IllegalStateException("No callback available for method " + method.getName());
}
```

对@Bean方法的拦截是通过BeanMethodInterceptor类作用的

### 3.BeanMethodInterceptor解析

(1)会调用isMatch方法判断代理类调用方法时是否进入对应的拦截器，如果调用的方法上面有@Bean注解就会进入BeanMethodInterceptor的intercept()方法

```java
/**
 * 判断该方法是否加了@Bean注解 若加了走BeanMethodInterceptor拦截
 * @param candidateMethod
 * @return
 */
@Override
public boolean isMatch(Method candidateMethod) {
   return (candidateMethod.getDeclaringClass() != Object.class &&
         !BeanFactoryAwareMethodInterceptor.isSetBeanFactory(candidateMethod) &&
         BeanAnnotationHelper.isBeanAnnotated(candidateMethod));
}
```

(2)进入intercept()后

 根据当前的method属性获取方法名或者method上@Bean的属性值name，作为BeanDefinition的名字

 1.若是一个普通bean则调用，最终调用   beanFactory.getBean(beanName)保证实列的唯一性

```java
//若不是FactoryBean,是个普通bean
return resolveBeanReference(beanMethod, beanMethodArgs, beanFactory, beanName);
//beanFactory.getBean从缓存中取单列保证唯一性
Object beanInstance = (useArgs ? beanFactory.getBean(beanName, beanMethodArgs) :
                     beanFactory.getBean(beanName));
```

2.若这个bean是一个factoryBean，会先从单列池中获取到factoryBean保证这个factoryBean的唯一性，同时会给这个factoryBean创建代理对象，抱在factoryBean.getObject（）时保证getObject()返回对象的唯一性

```java
//获取factoryBean
Object factoryBean = beanFactory.getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
 //若为factoryBean调用getObject方法，为factoryBean创建代理对象
return createCglibProxyForFactoryBean(factoryBean, beanFactory, beanName);
```

当方法名为“getObject时”会走回调函数调用getBean()方法，否则直接调用方法

```
((Factory) fbProxy).setCallback(0, (MethodInterceptor) (obj, method, args, proxy) -> {
   //调用getObject方法时回调这个匿名对象
   if (method.getName().equals("getObject") && args.length == 0) {
      return beanFactory.getBean(beanName);
   }
   return proxy.invoke(factoryBean, args);
});
```