---
title: 深究Spring系列 (1) - 就...Get了个Bean
date: 2019-09-19 17:42:45
categories: [Spring]
---
曾听一位高级工程师吐槽: 面试者回答"Spring中Bean是如何创建的,getBean的过程是什么样的?"这个问题时, 语塞说不出话来, 最后憋出一句: "就...Get了个Bean".
他说完后组内笑声此起彼伏, 我却陷入深思. 严格来讲我也不是很清楚这里面到底发生了啥.
## Spring的领域模型
1. 容器领域模型(Context模型)
Spring的掌控域(或者服务域),对Spring核心领域模型(Bean模型)进行生命周期管理.
2. 核心领域模型(Bean模型)
Bean是应用运行时可执行的最小函数式单元,可以是一个属性单元,也可以是Java中的一个函数对象,一种对象式的为某种特殊行为而生的可复用的概念,不受职责或者大小的限制.
每个线程的真正执行者,整个会话生命周期的管理者,Spring对外暴露的核心实体.
3. 代理领域模型(Advisor模型)
Spring代理的执行依赖于Bean模型,Spring代理的生成,执行及选择都依赖于Spring自身定义的Advisor模型,只有符合Advisor模型的定义,才能生成Spring代理.

Spring注解: 
    AnnotationConfigApplicationContext extends GenericApplicationContext 
                                       implements AnnotationConfigRegistry 


![AnnotationConfigApplicationContext](/images/Spring源码/AnnotationConfigApplicationContext.png)
![AnnotationConfigApplicationContext](/images/Spring源码/DefaultListableBeanFactory.png)


## Spring上下文和容器
1. ApplicationContext: 整个容器的基本功能定义类. 利用代理的设计方法, 内部持有BeanFactory实例, 这个实例替它执行BeanFactory接口定义的功能.
2. AbstractApplicationContext: 整个容器的核心处理类, 真正的Spring容器执行者, 在内部使用模板方法, 实现了高复用,高扩展,实现了Spring的启动,停止,刷新,事件推送,BeanFactory方法的默认实现及虚拟机回调的注册.
3. GenericApplicationContext: 最容易构建Spring环境的实体类,涵盖了Spring Context的核心功能, 开箱即用.
4. AnnotationConfigApplicationContext: 利用GenericApplicationContext的封装性和简便性. 传入class数组创建一个可执行的上下文实例来构造一个可运行的Spring运行环境.
```java
//根据参数类型可以知道，其实可以传入多个annotatedClasses，但是这种情况出现的比较少
	public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
		//调用无参构造函数，会先调用父类GenericApplicationContext的构造函数
		//父类的构造函数里面就是初始化DefaultListableBeanFactory，并且赋值给beanFactory
		//本类的构造函数里面，初始化了一个读取器：AnnotatedBeanDefinitionReader reader，一个扫描器ClassPathBeanDefinitionScanner scanner
		//scanner的用处不是很大，它仅仅是在我们外部手动调用 .scan 等方法才有用，常规方式是不会用到scanner对象的
		this();
		//把传入的类进行注册，这里有两个情况，
		//传入传统的配置类
		//传入bean（虽然一般没有人会这么做
		//看到后面会知道spring把传统的带上@Configuration的配置类称之为FULL配置类，不带@Configuration的称之为Lite配置类
		//但是我们这里先把带上@Configuration的配置类称之为传统配置类，不带的称之为普通bean
		register(annotatedClasses);
		//刷新
		refresh();
	}

```
5. AbstractRefreshableApplicationContext: 继承自AbstractApplicationContext, 是XmlWebApplicationContext的核心父类,如果当前上下文持有BeanFactory,则关闭当前BeanFactory,然后为上下文生命周期的下一个阶段初始化一个新的BanFactory, 并且在创建新容器时仍然保持对其父容器的引用.
6. EmbeddedWebApplicationContext: Spring Boot中新增的上下文实现类,是使用自嵌容器启动web应用的核心上下文基类.

## BeanFactory的设计
1. BeanFactory: 职责定义接口, 定义了按照名称,参数,类型等几个维度获取,判断Bean实例的职能
2. HierarchicalBeanFactory: 定义了父容器及判断当前Bean名称是否在当前Bean工厂中.
3. ConfigurableBeanFactory: 提供了设置父容器接口,指定类加载器的职能, 并且为当前容器工厂设计Bean的定制型的解析处理器,类型处理器等,实现对BeanFactory的可配置型
4. AutowireCapableBeanFactory: 提供了Bean的创建,注入职能, 提供了对Bean初始化前后的扩展性处理职能. 主要职责是处理在当前工厂中注册的Bean实例并使其达到可用状态
5. ListableBeanFactory: 对Bean实例的枚举,对有某些共同特征的Bean的管理,按照Bean名称,Bean实例,Bean类型获取Bean实例
6. ConfigurableListableBeanFactory: 集成ListableBeanFactory,AutowireCapableBeanFactory,ConfigurableBeanFactory接口所有职能,扩展了修改Bean定义信息和分析Bean的功能,实现了预实例化单例Bean及冻结当前工厂配置等功能.
7. AbstractBeanFactory: 实现了对基本容器功能定义的模板式封装和实现. 同时实现了对Bean信息的注册, 对Bean的创建及Bean定义描述信息相关的处理使用了抽象画处理的方式并交由继承者实现.
8. AbstractAutowireCapableBeanFactory: 解决Bean之间的依赖和注入问题,实现了Bean的创建方法.
9. DefaultListableBeanFactory: 提供了对Bean容器的完全成熟的默认实现, 可以直接对外使用.许多依赖BeanFactory的场景都通过DefaultListableBeanFactory类来实现对Bean的管理,注入,依赖解决,创建,销毁等功能
10. XmlBeanFactory: 集成DefaultListableBeanFactory并且内部持有XmlBeanDefinitionReader属性的Bean工厂容器(实现对XML文件定义的Bean描述信息的加载解析和处理)

## Spring父子上下文和容器
ApplicationContext中持有BeanFactory实例
子容器可以共用父容器中的Bean, 父容器不能共用子容器的Bean, 因为当父容器已经启动时,子容器还没有实例化并启动.

## Spring加载机制的设计和实现
