---
title: nodejs
date: 2019-04-04 12:01:22
tags: [nodejs]
categories: [nodejs]
---


Nodejs适合I/O密集型应用,而不适合计算密集型应用.

全局变量:
    process:
        process.argv 命令行参数数组,第一个元素是node,第二个是脚本名称,之后是运行参数
        process.stdout
        process.stdin
        process.nextTick(callback) 为事件循环设置一项任务,Node.js会在下次事件循环调响应
                                                      时调用callback.
                    目的: 缩短事件的执行时间                                           
        process.platform    
        process.pid    
        process.execPath    
        process.memoryUsage()    

    util:
        util.inherits 
    event:
        EventEmitter.on(event, listener)
        EventEmitter.emit(event, [args])
        EventEmitter.once(event, listener) 单次监听器,触发后解除.
        EventEmitter.removeListener(event, listener)
        EventEmitter.removeAllListener(event)
        

