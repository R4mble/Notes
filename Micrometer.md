---
title:  Meter
date: 2019-06-29 17:55:26
tags: [ Meter]
categories: [ Meter]
---
记录 Java 应用性能指标


计量器注册表 MeterRegistry
    创建和维护计量器.

    基于内存的计量器注册表实现: 
        SimpleMeterRegistry 不支持导出数据到监控系统

    可以组合多个计量器注册表的实现类
        CompositeMeterRegistry 运行同时发布数据到多个监控系统

```java
public class CompositeMeterRegistryExample {
 
  public static void main(String[] args) {
    CompositeMeterRegistry registry = new CompositeMeterRegistry();
    registry.add(new SimpleMeterRegistry());
    registry.add(new SimpleMeterRegistry(new MyConfig(), Clock.SYSTEM));
 
    Counter counter = registry.counter("simple");
    counter.increment();
  }
 
  private static class MyConfig implements SimpleConfig {
 
    public String get(final String key) {
      return null;
    }
 
    public String prefix() {
      return "my";
    }
  }
}
```
    静态的全局计量器注册表对象 
        Metrics.globalRegistry 组合注册表


```java
public class GlobalRegistryExample {
 
  public static void main(String[] args) {
    Metrics.addRegistry(new SimpleMeterRegistry());
    Counter counter = Metrics.counter("simple");
    counter.increment();
  }
}
```    


计量器 Meter
    需要收集的性能指标数据.

    计量器名称和标签
        每个计量器在创建时都可以指定一系列标签。标签以名值对的形式出现。监控系统使用标签对数据进行过滤。除了每个计量器独有的标签之外，每个计量器注册表还可以添加通用标签。所有该注册表导出的数据都会带上这些通用标签。

计数器 Counter
    单个只允许增加的值.    

计量仪 Gauge
    单个变化的值. Gauge对象既可以从计量器注册表中创建,也可以使用
    Gauge.builder()构建.

计时器 Timer
    记录事件的持续时间.

集成SpringBoot
        
