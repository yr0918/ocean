
Github源码：https://github.com/Netflix/eureka
http://blog.csdn.net/neosmith/article/details/53131023
http://blog.csdn.net/neosmith/article/details/52912645
http://www.idouba.net/sping-cloud-and-netflix/


http://blog.csdn.net/neosmith/article/details/53126924

## 1.服务注册/发现理论知识

## 2.Netflix Eureka
1. 是纯正的 servlet 应用，需构建成war包部署
2. 使用了 Jersey 框架实现自身的 RESTful HTTP接口
3. peer之间的同步与服务的注册全部通过 HTTP 协议实现
4. 定时任务(发送心跳、定时清理过期服务、节点同步等)通过 JDK 自带的 Timer 实现
5. 内存缓存使用Google的guava包实现

### 2.1代码结构
eureka-core 模块包含了功能的核心实现:

1. com.netflix.eureka.cluster - 与peer节点复制(replication)相关的功能
2. com.netflix.eureka.lease - 即”租约”, 用来控制注册信息的生命周期(添加、清除、续约)
3. com.netflix.eureka.registry - 存储、查询服务注册信息
4. com.netflix.eureka.resources - RESTful风格中的”R”, 即资源。相当于SpringMVC中的Controller
5. com.netflix.eureka.transport - 发送HTTP请求的客户端，如发送心跳
6. com.netflix.eureka.aws - 与amazon AWS服务相关的类

eureka-client模块:

Eureka客户端，微服务通过该客户端与Eureka进行通讯，屏蔽了通讯细节

eureka-server模块:

包含了 servlet 应用的基本配置，如 web.xml。构建成功后在该模块下会生成可部署的war包

### 2.2关键技术点

#### 2.2.1 Client选举算法

#### 2.2.2 Client与Server的心跳

#### 2.2.3 负载均衡Server节点之间同步

#### 2.2. REST FULL接口
https://github.com/Netflix/eureka/wiki/Eureka-REST-operations

## 3.Spring Cloud Eureka

## 4.常用服务治理工具比较
Eureka,Zookeeper,Consol