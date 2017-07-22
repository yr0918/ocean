# Hazelcast内存数据库(In-Memory Data Grid (IMDG))
官网：https://hazelcast.org/
文档：http://docs.hazelcast.org/docs/3.7.7/manual/html-single/index.html

缓存那些事
https://tech.meituan.com/cache_about.html

memcached各种应用分析
http://blog.csdn.net/huilangeliuxin/article/details/38895607

分布式缓存服务器设计原理
http://www.cnblogs.com/liulun/p/3625941.html

分布式缓存hazelcast的一些原理分析
http://wanghongji.iteye.com/blog/2047740

Hazelcast集群原理分析
http://blog.csdn.net/wangyangzhizhou/article/details/52677826

## 概述

1. 采用本地缓存+分布式缓存的方式，优先访问本地缓存，没有命中就会远程获取，这样有一定的提速
1. 集成方便，可以方便的与spring和，Hibernate 提供可视化的管理平台

```
1.内存的大小是否无限制的获取？还是可以控制最大使用内存？
2.
```

## Hazelcast运行结构

![](img/hazelcast.runtime.jpg)

在p2p模式中，所有的节点（Node）都是集群中的服务节点，提供相同的功能和计算能力。每个节点都分担集群的总体性能，每增加一个节点都可以线性增加集群能力。

在p2p服务集群的基础上，我们可以增加许多客户端接入到集群中，这样就形成了集群的C/S模式，提供服务集群视作S端，接入的客户端视作C端。这些客户端不会分担集群的性能，但是会使用集群的各种资源。下图的结构就是客户端接入集群的情况。

## Hazelcst组网
Hazelcast自称"分布式数据网格”，那他最基本、最重要的功能就是时时刻刻都在多台服务器之间工作，这样必须有网络环境对其分布式功能提供支持。Hazelcast在网络环境中工作分为2个阶段：首先是组网阶段，随后是数据传输阶段。

组网是指每个Hazelcast节点启动时，都会搜寻是否有Hazelcast节点可以连接，组网过程支持多种协议。完成组网后，节点会和其他组建成集群的节点进行通信，这个阶段就是数据传输阶段，此时只支持使用TCP/IP协议来传递数据。Hazelcast的所有网络行为，都是通过<networt></network>元素配置决定的。<join>元素用来配置组建集群的相关的参数

`组播协议（Multicast）组建集群`

在使用组播协议（Multicast）作为自动组建集群机制时，集群中的成员不需要知道其他成员的详细地址（IP），他们仅仅是通过组播将信号广播到其他成员的监听端口中。使用之前确保网络环境支持 Multicast

`TCP协议组建集群`

除了使用 组播协议，还可以使用TCP/IP协议来组建集群。当使用TCP/IP来组建新集群时，第一个节点必须将所有要加入集群的节点IP地址添加到对应列表中。在集群已经运行之后，新加入的节点不必知道所有的集群节点，但是至少要知道并连接到一个已经启动的集群节点

## 原理
Hazelcast is peer-to-peer. There is no master and slave; there is no single point of failure.
All members store equal amounts of data and do equal amounts of processing

You can embed Hazelcast in your existing application or use it in client and server mode where your application is a client to Hazelcast members

Hazelcast is designed to scale up to hundreds and thousands of members. Simply add new members and they will automatically discover the cluster
and will linearly increase both memory and processing capacity. The members maintain a TCP connection between each other and all communication
is performed through this layer

Hazelcast keeps the backup of each data entry on multiple members. On a member failure, the data is restored from the backup and the cluster
will continue to operate without downtime

Hazelcast distributes key objects into partitions using a consistent hashing algorithm,Hazelcast shards are called Partitions. By default,
Hazelcast has 271 partitions. Given a key, we serialize, hash and mode it with the numberof partitions to find the partition which the key
belongs to. The partitions themselves are distributed equally among the members of the cluster.Hazelcast also creates the backups of partitions
and distributes them among members for redundancy

Distributed Data Structures
1. Standard utility collections：Map,Queue,Ringbuffer,Set,List,MultiMap,Replicated Map
2. Topic
3. Concurrency utilities:Lock,Semaphore,AtomicLong,AtomicReference,IdGenerator,CountdownLatch

Partition Table
Repartitioning
Discovering Cluster Members
 Discovering Members by Multicast
 Discovering Members by TCP
 ..
Getting a Map and Putting an Entry
Creating Sync Backups
Enabling Backup Reads
Evicting Map Entries
Loading and Storing Persistent Data

reading Creating Near Cache for Map


## 路由算法(新增、删除节点分析)

## 数据同步

## Leader选举