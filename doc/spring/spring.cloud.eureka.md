
Github源码：
https://github.com/Netflix/eureka

深度剖析服务发现组件Netflix Eureka
http://techshow.ctrip.com/archives/1699.html?utm_source=tuicool&utm_medium=referral

Netflix Eureka 深层解析（上）
http://blog.csdn.net/u011834741/article/details/54694045

Netflix Eureka 深层解析（下）
http://blog.csdn.net/u011834741/article/details/54709209


## 1.服务注册/发现理论知识

## 2.Netflix Eureka
1. 是纯正的 servlet 应用，需构建成war包部署
2. 使用了 Jersey 框架实现自身的 RESTful HTTP接口
3. peer之间的同步与服务的注册全部通过 HTTP 协议实现
4. 定时任务(发送心跳、定时清理过期服务、节点同步等)通过 JDK 自带的 Timer 实现
5. 内存缓存使用Google的guava包实现

### 2.2关键技术点

1. server端如何启动的？
2. server与server端如何同步数据的？
3. client如何注册到server中？
4. client是如何选择哪个server做心跳的？
5. client(customer)是如何获取client(provider)的？
6. 路由表数据如何缓存在server、client端的？
7. 自我保护机制(self-preservation mode)是什么概念，为什么要这么设计？
8. 增量更新(delta updates)是什么概念, 为什么要这么设计？

#### 2.2. REST FULL接口
https://github.com/Netflix/eureka/wiki/Eureka-REST-operations
<table>
	<tbody><tr>
		<td> <strong>Operation</strong> </td>
		<td> <strong><span>HTTP</span> action</strong> </td>
		<td> <strong>Description</strong> </td>
	</tr>
	<tr>
		<td> Register new application instance </td>
		<td> <span>POST</span> /eureka/v2/apps/<b>appID</b> </td>
		<td> Input: <span>JSON</span>/<span>XML</span> payload <span>HTTP</span> Code: 204 on success </td>
	</tr>
	<tr>
		<td> De-register application instance </td>
		<td> <span>DELETE</span> /eureka/v2/apps/<b>appID</b>/<b>instanceID</b> </td>
		<td> <span>HTTP</span> Code: 200 on success </td>
	</tr>
	<tr>
		<td> Send application instance heartbeat </td>
		<td> <span>PUT</span> /eureka/v2/apps/<b>appID</b>/<b>instanceID</b> </td>
		<td> <span>HTTP</span> Code:<br>
* 200 on success<br>
* 404 if <b>instanceID</b> doesn’t exist </td>
	</tr>
	<tr>
		<td> Query for all instances </td>
		<td> <span>GET</span> /eureka/v2/apps </td>
		<td> <span>HTTP</span> Code: 200 on success Output: <span>JSON</span>/<span>XML</span>
</td>
	</tr>
	<tr>
		<td> Query for all <b>appID</b> instances </td>
		<td> <span>GET</span> /eureka/v2/apps/<b>appID</b> </td>
		<td> <span>HTTP</span> Code: 200 on success Output: <span>JSON</span>/<span>XML</span> </td>
	</tr>
	<tr>
		<td> Query for a specific <b>appID</b>/<b>instanceID</b> </td>
		<td> <span>GET</span> /eureka/v2/apps/<b>appID</b>/<b>instanceID</b> </td>
		<td> <span>HTTP</span> Code: 200 on success Output: <span>JSON</span>/<span>XML</span>
</td>
	</tr>
	<tr>
		<td> Query for a specific <b>instanceID</b> </td>
		<td> <span>GET</span> /eureka/v2/instances/<b>instanceID</b> </td>
		<td> <span>HTTP</span> Code: 200 on success Output: <span>JSON</span>/<span>XML</span>
</td>
	</tr>
	<tr>
		<td> Take instance out of service </td>
		<td> <span>PUT</span> /eureka/v2/apps/<b>appID</b>/<b>instanceID</b>/status?value=OUT_OF_SERVICE</td>
		<td> <span>HTTP</span> Code:<br>
* 200 on success<br>
* 500 on failure </td>
	</tr>
	<tr>
		<td> Put instance back into service (remove override) </td>
		<td> <span>DELETE</span> /eureka/v2/apps/<b>appID</b>/<b>instanceID</b>/status?value=UP  (The value=UP is optional, it is used as a suggestion for the fallback status due to removal of the override)</td>
		<td> <span>HTTP</span> Code:<br>
* 200 on success<br>
* 500 on failure </td>
	</tr>
	<tr>
		<td> Update metadata </td>
		<td> <span>PUT</span> /eureka/v2/apps/<b>appID</b>/<b>instanceID</b>/metadata?key=value</td>
		<td> <span>HTTP</span> Code:<br>
* 200 on success<br>
* 500 on failure </td>
	</tr>
	<tr>
		<td> Query for all instances under a particular <b>vip address</b> </td>
		<td> <span>GET</span> /eureka/v2/vips/<b>vipAddress</b> </td>
		<td> <br>
* <span>HTTP</span> Code: 200 on success Output: <span>JSON</span>/<span>XML</span> <br>
* 404 if the <b>vipAddress</b> does not exist.</td>
	</tr>
	<tr>
		<td> Query for all instances under a particular <b>secure vip address</b> </td>
		<td> <span>GET</span> /eureka/v2/svips/<b>svipAddress</b> </td>
		<td> <br>
* <span>HTTP</span> Code: 200 on success Output: <span>JSON</span>/<span>XML</span> <br>
* 404 if the <b>svipAddress</b> does not exist.</td>
	</tr>
</tbody>
</table>

## 3.Spring Cloud Eureka

https://segmentfault.com/a/1190000008378268

## 4.常用服务治理工具比较
Eureka,Zookeeper,Consol
http://www.tuicool.com/articles/zyy2qeZ

<table>
    <thead>
      <tr>
        <th width="26%">Feature</th>
        <th width="21%">Consul</th>
        <th width="17%">zookeeper</th>
        <th width="18%">etcd</th>
        <th width="18%">euerka</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>服务健康检查</td>
        <td>服务状态，内存，硬盘等</td>
        <td>(弱)长连接，keepalive</td>
        <td>连接心跳</td>
        <td>可配支持</td>
      </tr>
      <tr>
        <td>多数据中心</td>
        <td>支持</td>
        <td>—</td>
        <td>—</td>
        <td>—</td>
      </tr>
      <tr>
        <td>kv存储服务</td>
        <td>支持</td>
        <td>支持</td>
        <td>支持</td>
        <td>—</td>
      </tr>
      <tr>
        <td>一致性</td>
        <td>raft</td>
        <td>paxos</td>
        <td>raft</td>
        <td>—</td>
      </tr>
      <tr>
        <td>cap</td>
        <td>ca</td>
        <td>cp</td>
        <td>cp</td>
        <td>ap</td>
      </tr>
      <tr>
        <td>使用接口(多语言能力)</td>
        <td>支持http和dns</td>
        <td>客户端</td>
        <td>http/grpc</td>
        <td>http（sidecar）</td>
      </tr>
      <tr>
        <td>watch支持</td>
        <td>全量/支持long polling</td>
        <td>支持</td>
        <td>支持 long polling</td>
        <td>支持 long polling/大部分增量</td>
      </tr>
      <tr>
        <td>自身监控</td>
        <td>metrics</td>
        <td>—</td>
        <td>metrics</td>
        <td>metrics</td>
      </tr>
      <tr>
        <td>安全</td>
        <td>acl /https</td>
        <td>acl</td>
        <td>https支持（弱）</td>
        <td>—</td>
      </tr>
      <tr>
        <td>spring cloud集成</td>
        <td>已支持</td>
        <td>已支持</td>
        <td>已支持</td>
        <td>已支持</td>
      </tr>
    </tbody>
  </table>

1. `服务的健康检查`：
Euraka 使用时需要显式配置健康检查支持；Zookeeper,Etcd 则在失去了和服务进程的连接情况下任务不健康，而 Consul 相对更为详细点，比如内存是否已使用了90%，文件系统的空间是不是快不足了。

1. `多数据中心支持`：
Consul 通过 WAN 的 Gossip 协议，完成跨数据中心的同步；而且其他的产品则需要额外的开发工作来实现；

1. `KV 存储服务`：
除了 Eureka ,其他几款都能够对外支持 k-v 的存储服务，所以后面会讲到这几款产品追求高一致性的重要原因。而提供存储服务，也能够较好的转化为动态配置服务哦。

1. `产品设计中 CAP 理论的取舍`：
Eureka 典型的 AP,作为分布式场景下的服务发现的产品较为合适，服务发现场景的可用性优先级较高，一致性并不是特别致命。其次 CA 类型的场景 Consul,也能提供较高的可用性，并能 k-v store 服务保证一致性。 而Zookeeper,Etcd则是CP类型 牺牲可用性，在服务发现场景并没太大优势；

1. `多语言能力与对外提供服务的接入协议`：
Zookeeper的跨语言支持较弱，其他几款支持 http11 提供接入的可能。Euraka 一般通过 sidecar的方式提供多语言客户端的接入支持。Etcd 还提供了Grpc的支持。 Consul除了标准的Rest服务api,还提供了DNS的支持。

1. `Watch的支持（客户端观察到服务提供者变化）`：
Zookeeper 支持服务器端推送变化，Eureka 2.0(正在开发中)也计划支持。 Eureka 1,Consul,Etcd则都通过长轮询的方式来实现变化的感知；

1. `自身集群的监控`：
除了 Zookeeper ,其他几款都默认支持 metrics，运维者可以搜集并报警这些度量信息达到监控目的；

1. `安全`：
Consul,Zookeeper 支持ACL，另外 Consul,Etcd 支持安全通道https.

1. `Spring Cloud的集成`：
目前都有相对应的 boot starter，提供了集成能力。

总的来看，目前Consul 自身功能，和 spring cloud 对其集成的支持都相对较为完善，而且运维的复杂度较为简单（没有详细列出讨论），Eureka 设计上比较符合场景，但还需持续的完善
## 5.最佳实践
参数配置的最佳实践：
https://github.com/spring-cloud/spring-cloud-netflix/issues/203

多网卡环境下Eureka服务注册IP选择问题
http://blog.csdn.net/neosmith/article/details/53126924