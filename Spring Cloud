Spring Cloud
    Spring Cloud Config
        配置管理工具包
    Spring Cloud Netflix
        包含Netflix OSS的基础组件:
            Eureka: 云端服务管理,基于REST
            Hystrix: 断路器,容错机制,降级机制
            Zuul: 动态路由,监控,安全,过滤
            Ribbon: 云端负载均衡
            Feign: REST客户端

Ribbon
    负载均衡器:
        功能:
            维护服务器的IP,DNS名称等
            根据特定的逻辑在服务器列表中循环
        子模块:
            Rule: 逻辑组件,决定从服务器列表中返回哪个服务器实例.
                默认使用RoundRobinRule,轮询服务器列表.
                AvailabilityFilteringRule: 忽略无法连接的服务器,忽略并发数过高的服务器.
                WeightedResponseTimeRule: 根据服务器的响应时间,设置权重值.
                ZoneAvoidanceRule: 根据区域(机架,机房),可用服务器为基础选择服务器.
                BestAvailableRule: 忽略短路的服务器,选择并发数较低的服务器.
                RandomRule: 随机选.
                RetryRule: 如果使用RoundRobinRule选择的服务器无法连接,重新选择服务器.

            Ping: 使用定时器来确保服务器网络可以连接.每隔一段时间Ping服务器,判断是否存活.


            ServerList: 服务器列表,可以通过静态的配置确定负载的服务器,也可以
                        动态指定,通过动态指定时会有后台的线程来刷新该列表.
            
Feign   调用REST服务
    使用JDK的动态代理,生成的代理类将请求的信息封装,交给feign.Client接口发送请求,该接口的默认实现类
    最终使用java.net.HttpURLConnection来发送HTTP请求.
    

