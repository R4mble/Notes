核心组件:
    etcd: 保存整个集群的状态
    apiserver: 提供资源操作的唯一入口,提供认证,授权,访问控制,API注册和发现等机制
    controller manager: 维护集群的状态, 如故障检测,自动扩展,滚动更新等.
    scheduler: 负责资源调度,按照预定的调度策略将Pod调度到相应的机器上.
    kubelet: 负责维护容器的生命周期,也负责Volume(CVI)和网格(CNI)的管理.
    Container runtime: 负责镜像管理以及Pod和容器的真正运行(CRI)
    kube-proxy: 负责为Service提供cluster内部的服务发现和负载均衡.
    
Add-ons:
    kube-dns: 负责为整个集群提供DNS服务
    Ingress Controller: 为服务提供外网入口.
    Heapster: 资源监控

API对象: Kubernetes集群中的管理操作单元.
    三大类属性: 元数据metadata
                 标识API对象,至少包含:namespace,name和uid. 使用标签来标识和匹配不同的对象.
                                如env=dev来标识开发服务.
              规范spec
                 描述了用户期望的Kubernetes集群中的分布式系统达到的理想状态.如通过Replication Controller设置
                    期望的Pod副本数为3.
              状态status
                 描述了系统实际当前达到的状态,如当前实际Pod副本数为2

Pod: Kebernetes集群中运行部署应用或服务的最小单元.
        支持多个容器在一个Pod中共享网络地址和文件系统,可以通过进程间通信和
        文件共享的方式完成服务.
                      

kubectl get nodes
    获取可用的节点.