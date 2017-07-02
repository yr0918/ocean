
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

## 5.最佳实践
参数配置的最佳实践：
https://github.com/spring-cloud/spring-cloud-netflix/issues/203

多网卡环境下Eureka服务注册IP选择问题
http://blog.csdn.net/neosmith/article/details/53126924