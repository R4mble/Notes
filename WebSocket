相关技术
    向客户端广播或者推送消息: Server-Sent Event(SSE),提供自动重连,事件ID等功能.
    扩充HTTP的网络协议: SPDY, 压缩HTTP首标,多路复用,工作管道.
    Web实时通信: Web Real-Time Communication(WebRTC)浏览器之间实时通信,实时语音和视频聊天.

WebSocket
    事件驱动,异步编程,支持二进制数据和文本数据.    

    构造函数: URL和protocols
        URL可以是ws或wss, 分别用于非加密和加密流量.wss使用SSL,就是HTTPS的安全机制来保证.
        protocols可以是XMPP,SOAP,自定义协议等.客户端定义通信协议,服务器选择一种.

        客户发送带有协议名称的Sec-WebSocket-Protocol首标,服务器响应一个带有和客户请求相同的
        协议名称的Sec-WebSockt-Protocol首标.

    WebSocket事件:
        open, message, error, close

    二进制数据类型: ws.binaryType: Blob(多媒体文件,图像,视频,音频), ArrayBuffer(Uint8Array)

    WebSocket方法
        send()
            onopen之后,onclose之前.  当连接不可用时,抛出一个有关无效连接状态的异常.
        close()
            终止连接, 可选参数: code(数字型状态码), reason(文本字符串)

    WebSocket对象特性 
        readyState
            0: WebSocket.CONNECTING
            1: WebSocket.OPEN
            2: WebSocket.CLOSING
            3: WebSocket.CLOSED
        bufferedAmount
            检查已进入队列,但尚未发送到服务器的字节数,不包括协议组帧开销或操作系统,网络硬件所进行的缓冲.
            可以限制应用向服务器发送的速率,避免网络饱和.
            关闭连接前也可以检查,看是否还有数据没有发送到服务器.
        protocol
            服务器选择的协议名,在握手完成之前为空.

    WebSocket初始握手
        客户端:
            GET /echo /HTTP/1.1
            Host: echo.websocket.org
            Origin: http://www.websocket.org
            Sec-WebSocket-Key: ah43rfdidajureqf
            Sec-WebSocket-Version: 13
            Upgrade: websocket
        服务器:
            101 Switching Protocols
            Connection: Upgrade
            Date: Wed, 20 Jun 2012 03:23:13 GMT
            Sec-WebSocket-Accepct: da+fdlfskjoewi
            Server: Kaazing Gateway
            Upgrade: WebSocket

        计算响应键值
            WebSocket服务器响应一个计算出来的键值,从客户端发送的Sec-WebSocket-Key首标中
            取得键值,在Sec-WebSocket-Accepct首标中返回根据客户端预期计算的键值.

    消息格式
        
消息和帧
    

26. Spring对WebSocket的支持
    26.1 介绍
    26.2 服务端的Spring WebSocket API

        WebSocketHandler接口: WebSocket消息和生命周期事件处理器.
            void afterConnectionEstablished(WebSocketSession session) throws Exception;
                在WebSocket协定成功并且连接建立准备好使用时执行
            void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception;
                在新消息到达时执行
            void handleTransportError(WebSocketSession session, Throwable exception) throws Exception;
                处理WebSocket消息传输下层错误  
            void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception;            
                连接关闭或者发生传输错误时执行.
            boolean supportsPartialMessages();
                WebSocketHandler是否处理部分消息.

        AbstractWebSocketHandler抽象类: 重写了handleMessage()方法,将三种message分配给三个新建的处理方法.
                                       重写了supportsPartialMessages()方法, 直接返回false
                                       其它的都是空实现.

        BinaryWebSocketHandler和TextWebSocketHandler:
            继承自AbstractWebSocketHandler, 重写对方的消息处理方法, 关闭会话.                             

        WebSocketSession接口: WebSocket会话抽象,允许在WebSocket连接之上发送消息然后关闭它.
            String getId();                          返回会话的独特标识符
            URI getUri();                            返回建立WebSocket连接的URI
            HttpHeaders getHandshakeHeaders();       返回握手请求中使用的Headers
            Map<String, Object> getAttributes();     返回WebSocket会话属性
            Principal getPrincipal();                返回包含已认证用户的姓名的Principal实例,如果用户未认证,返回null.
            InetSocketAddress getLocalAddress();     返回请求被接受的地址
            InetSocketAddress getRemoteAddress();    返回远程客户端的地址
            String getAcceptedProtocol();            返回协定的子协议
            void setTextMessageSizeLimit(int messageSizeLimit); 配置接收文本消息的最大值
            int getTextMessageSizeLimit();           获取已配置的接收文本消息的最大值
            void setBinaryMessageSizeLimit(int messageSizeLimit); 配置接收二进制消息的最大值
            int getBinaryMessageSizeLimit();         获取已配置的接收二进制消息的最大值
            List<WebSocketExtension> getExtensions(); 确定协定的拓展
            void sendMessage(WebSocketMessage<?> message) throws IOException; 发送WebSocket消息.
            boolean isOpen();                         返回连接是否依然开放
            void close() throws IOException;          以1000的状态码关闭WebSocket连接(CloseStatus.NORMAL)
            void close(CloseStatus status) throws IOException; 以给定的的状态码关闭WebSocket连接

        WebSocketMessage<T>接口: 可以在WebSocket连接上处理和发送的消息
            T getPayload();             返回消息的负载
            int getPayloadLength();     返回消息字节数
            boolean isLast();           如果当前消息是客户端发送的完整的WebSocket消息的最后一部分,返回true.
                                        如果部分消息支持不可用或者未开启,返回false

        WebSocketConfigurer接口: 定义回调方法来配置WebSocket的请求处理.
            void registerWebSocketHandlers(WebSocketHandlerRegistry registry);
                注册WebSocketHandler

        WebSocketHandlerRegistry接口: 提供配置WebSocketHandler请求映射的方法.
            WebSocketHandlerRegistration addHandler(WebSocketHandler webSocketHandler, String... paths);
                在指定的URL路径上配置WebSocketHandler

        WebSocketHandlerRegistration接口: 提供配置WebSocket Handler的方法.
            WebSocketHandlerRegistration addHandler(WebSocketHandler handler, String... paths);
                添加共享相同配置(interceptors,SockJS,config)的handler.
            WebSocketHandlerRegistration setHandshakeHandler(HandshakeHandler handshakeHandler);
                配置HandshakeHandler
            WebSocketHandlerRegistration addInterceptors(HandshakeInterceptor... interceptors);
                配置握手请求的拦截器
            WebSocketHandlerRegistration setAllowedOrigins(String... origins);
                配置被允许的header值
            SockJsServiceRegistration withSockJS();
                开启SockJS fallback选项

    26.4 STOMP协议的介绍,配置,常用注解,测试
            WebSocket子协议,定义可以发送的消息类型,格式和内容.面向文本的协议,但是消息负载可以是文本也可以是二进制.
            基于帧的协议,帧结构:
                COMMAND
                header1:value1
                header2:value2

                Body^@
            客户端可以使用SEND或者SUBSCRIBE命令来发送或者订阅消息,同时使用destination的header来描述消息的相关信息和
            接收地址.


            Message<T>接口: 有headers和body的通用消息表示.
                T getPayload();                 返回消息负载
                MessageHeaders getHeaders();    返回消息headers
            MessageSendingOperations接口: 发送消息到目标的操作.
                void send(Message<?> message) throws MessagingException;
                    发送消息到默认目标
                void send(D destination, Message<?> message) throws MessagingException;
                    发送消息到给定目标
                void convertAndSend(Object payload) throws MessagingException;
                    转换后发送
            
            常用注解:
                @EnableWebSocketMessageBroker: 在WebSocket之上使用高级的消息子协议开启broker-backed消息
                @MessageMapping: 映射Message到消息处理方法上.
                @SendTo

            常用接口和类:
                WebSocketMessageBrokerConfigurer接口: 定义在WebSocket客户端使用STOMP消息处理的方法.
                    default void registerStompEndpoints(StompEndpointRegistry registry) {}
                        注册STOMP端点到指定的URL

                StompEndpointRegistry接口: 在WebSocket端点之上注册STOMP
                StompWebSocketEndpointRegistration接口: 在WebSocket端点之上配置STOMP
                SimpMessagingTemplate类: STOMP消息发送器

    STOMP
        SEND帧:
            SEND
                destination:/queue/trade
                content-type:application/json
                content-length:44

                {"action":"BUY","ticker":"MMM","shares",44}^@