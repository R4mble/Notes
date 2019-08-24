---
title: JWT
date: 2019-04-24 02:30:28
---
1. 了解JWT相关知识
        JWT组成: 
            Header.Payload.Signature
            头部.负载.签名

        头部:
            {
               "alg": "HS256",
                "typ": "JWT"
            }
            alg表示签名的算法，默认是HMAC SHA256, typ表示这个令牌的类型.

        负载:
            iss (issuer)：签发人
            exp (expiration time)：过期时间
            sub (subject)：主题
            aud (audience)：受众
            nbf (Not Before)：生效时间
            iat (Issued At)：签发时间
            jti (JWT ID)：编号

        签名: 对前两部分的签名,防止数据篡改.
            HMACSHA256(
                base64UrlEncode(header) + "." +
                base64UrlEncode(payload),
                secret)
        把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"（.）分隔

Spring中使用JWT:
    1. 编写由登录对象转为token和由token获取登录对象的方法.
    2. 继承OncePerRequestFilter,重写doFilterInternal方法,对每一次请求进行token检查.
    3. 使用@AuthenticationPrincipal注解, 根据前端传过来的header获取登录对象.

2. postgresql的具体使用:
        读取数组属性:
            1. 定义ArrayTypeHandelr继承BaseTypeHandler,重写get,set方法
            2. 指定mybatis的类型处理器包的位置为ArrayTypeHandelr所在包.
            3. 定义数组属性所在类型的resultMap.


