@Autowired
    先根据类型找bean，如果对应类型的bean不唯一，则根据属性名称和bean的名称进行匹配。
    如果不确定其标注属性一定存在且允许为null，则可设置required属性为false
        @Autowired(required = false)
    可以用到方法或者方法参数上。

    @Primary
        多个同类型bean同时存在时，设置注入的优先级。
    
    @Qualifier
        与@Autowired配合，通过类型和名称一起找到Bean。

    也可以放在构造器参数前面，注入参数。

Bean的生命周期
    Bean定义
        1. 在@ComponentScan定义的扫描路径找带有@Component的类，资源定位。
        2. 解析资源，保存Bean定义的信息到BeanDefinition的实例中。
        3. 把Bean定义发布到Spring IoC容器中
        4. 默认情况下，Spring继续完成Bean的实例化和依赖注入。
    Bean生命周期
        @ComponentScan配置项lazyInit，默认为false，不进行延迟初始化。
    
        接口BeanNameAware setBeanName方法
        接口BeanFactoryAware setBeanFactory方法
        接口ApplicationContextAware(需要容器实现ApplicationContext接口) setApplicationContext方法
        BeanPostProcessor预初始化方法(针对全部Bean生效) postPorcessBeforeInitialization方法
        @PostConstruct标注方法 自定义初始化方法
        接口InitializingBean afterPropertiesSet方法
        BeanPostProcessor后初始化方法(针对全部Bean生效) postPorcessAfterInitialization方法
        生存期
        @PreDestroy标注方法 自定义销毁方法
        接口DisPosableBean destroy方法: 释放资源
            


文件上传
    
SessionCallback和RedisCallback接口
作用: 让RedisTemplate进行回调, 通过它们可以在同一条连接下执行
     多个Redis命令.避免RedisTemplate多次获取不同的连接

     SessionCallback封装良好,应当优先使用.
     RedisCallback比较底层,要处理的内容多,可读性差.

    SessionCallback<T>: 
        <K, V> T execute(RedisOperations<K, V> operations) throws DataAccessException;
            执行同一个session中的所有操作.

ThreadPoolTaskExecutor类:
