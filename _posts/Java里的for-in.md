---
title: Java里的for in
date: 2019-04-05 02:42:51
tags: [Java]
---
 ```java       
Element[] ele = new Element[5];

for (Element e : ele) {
    e = new Element();
    e.inner = "fuck js";
}
```

> 请问: 上面的ele数组中的元素被实例化了吗?

> 答案: `没有`


<i class="icon-reorder"></i>把for in 循环换成普通的for循环:

 ```java  
for (int i = 0; i < ele.length; i++) {
    ele[i] = new Element();
    ele[i].inner = "fuck js";
}
```

> 请问: 上面的ele数组中的元素被实例化了吗?

> 答案: `是的`

> 由此可见: 
>  - **for循环是值传导的,获取到的是原对象的拷贝.**
>  - **而数组索引就相当于指针,直接修改了内存中的值.**

所以自然地:

 ```java  
Element[] ele = new Element[5];

for (int i = 0; i < ele.length; i++) {
    ele[i] = new Element();
    ele[i].inner = "fuck js";
}

for (Element e : ele) {
    e = new Element();
    e.inner = "fuck java";
}

Arrays.stream(ele).map(e -> e.inner).forEach(System.out::println);
```

当然是"fuck js"啦!