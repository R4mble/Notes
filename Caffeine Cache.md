Cache接口: 定义通用缓存操作的接口
    获取缓存的名字
    获取底层的缓存提供者
    
CacheManager接口: spring的核心缓存管理服务提供接口
    获取给定名字的缓存
    获取此缓存管理器的所有缓存名字集合

    
ApplicationListener接口: 事件监听器, 观察者模式.
    void onApplicationEvent(E event); 处理事件

ContextRefreshedEvent类: 当ApplicationContext初始化或者刷新时该事件触发

CacheRefreshNotifier类: 
    利用PostgreSQL的Notify/Listen机制,配合触发器函数notify_data_change(), 监听表数据的增删改,对缓存进行同步刷新

注解:
    @EnableCaching: 开启缓存管理
    @Cacheable: 执行方法的结果可以被缓存
    @CacheEvict: 方法会触发清除缓存操作