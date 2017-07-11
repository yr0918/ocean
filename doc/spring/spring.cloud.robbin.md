Github: https://github.com/Netflix/ribbon

Spring Cloud源码分析（二）Ribbon
http://blog.didispace.com/springcloud-sourcecode-ribbon/

Ribbon is a client side IPC library that is battle-tested in cloud.Ribbon plays a critical role in supporting
inter-process communication in the cloud. The library includes the Netflix `client side load balancers` and clients
for middle tier services, It provides the following features

- Load balancing
- Fault tolerance
- Multiple protocol (HTTP, TCP, UDP) support in an asynchronous and reactive model
- Caching and batching

# Modules
- ribbon: APIs that integrate load balancing, fault tolerance, caching/batching on top of other ribbon modules and Hystrix
- ribbon-loadbalancer: Load balancer APIs that can be used independently or with other modules
- ribbon-eureka: APIs using Eureka client to provide dynamic server list for cloud
- ribbon-transport: Transport clients that support HTTP, TCP and UDP protocols using RxNetty with load balancing capability
- ribbon-httpclient: REST client built on top of Apache HttpClient integrated with load balancers
                       (deprecated and being replaced by ribbon module)，已经被遗弃，集成到了ribbon中
- ribbon-core: Client configuration APIs and other shared APIs

# 2.核心实现

## 负载规则(Multiple and pluggable load balancing rules)？
## 怎么与Eureka无缝结合的(Integration with service discovery)？

## Spring Cloud Robbin

1.怎么抽象一个loadbalacer的？
2.怎么集成ribbon和RestTemplate的？

## 最佳实践