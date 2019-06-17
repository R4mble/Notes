---
title: Spring MVC
date: 2019-04-05 02:39:49
tags: [Spring]
---
DispatcherServlet

    1.用户向服务器发送请求,被Spring的前端控制器DispatcherServlet截获.

    2.DispatcherServlet对请求URL进行解析,得到URI,调用HandlerMapping获得该
        Handler配置的所有相关的对象.包括Handler对象以及Handler对象对应的拦截器.
        这些对象被封装到一个HandlerExecutionChain对象当中返回

    3.DispatcherServlet根据Handler选择HandlerAdapter,调用实际处理请求的方法.

    4.提取请求中的模型数据,执行Handler.在填充Handler入参过程中:
        消息转换:   将json,xml等转换成一个对象,将对象转换为指定的响应消息
        数据转换:   对请求消息进行数据转换,String转换为Integer,Double等
        数据格式化:  如字符串转为格式化数字或格式化日期等
        数据验证:   验证数据的有效性,验证结果存到BindingResult或Error中

    5.Handler执行完成之后,向DispatcherServlet返回一个ModelAndView对象,包含视图名
        或视图名和模型

    6.根据ModelAndView选择一个合适的ViewResolver返回给DispatcherServlet

    7.ViewSolver结合Model和View来渲染视图

    8.将视图返回给客户端
