Observable
    被观察者, 决定什么时候触发事件和触发什么事件, 决定异步操作模块的顺序和次数.
Observer
    观察者, 可以在不同的线程中执行任务. 创建一个处于待命状态的观察者哨兵, 可以在未来某个时刻
    响应Observable的通知,不需要阻塞等待Observable发射数据.
subscribe
    subscribe(onNext)    
    subscribe(onNext, onError)    
    subscribe(onNext, onError, onComplete)       
    subscribe(onNext, onError, onComplete, onSubscribe)    

    onComplete在onNext之后执行, 它是一个Action, 无参数类型
    onSubscribe在onNext和onComplete之前执行

五种观察者模式
    Observable: 能够发射0或n个数据,并以成功或错误事件终止
    Flowable: 同上, 支持背压,可以控制数据源发射的速度
    Single: 只能发射单个数据或错误事件
    Completable: 不发射数据,只处理onComplete和onError事件
    Maybe: 能够发射0或1个数据,要么成功要么失败,类似Option

do操作符
    doOnSubscribe: 一旦观察者订阅了Observable, 就会被订阅
    doOnLifecycle: 在观察者订阅之后,设置是否取消订阅
    doOnNext: 它产生的Observable每发射一项数据就会调用它一次,它的Consumer接受发射的数据项.在subscribe
                之前对数据进行处理.
    doOnEach:
    doAfterNext:
    doOnComplete:
    doFinally:
    doAfterTerminate: 

Hot/Code Observable 
Hot: 