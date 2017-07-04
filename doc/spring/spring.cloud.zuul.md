
Wiki
https://github.com/Netflix/zuul/wiki

How it works
https://github.com/Netflix/zuul/wiki/How-it-Works

## Zuul




#### com.netflix.zuul.exception.ZuulException: Forwarding error错误
```
com.netflix.zuul.exception.ZuulException: Forwarding error
	at org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter.handleException(RibbonRoutingFilter.java:183) ~[spring-cloud-netflix-core-1.3.0.RELEASE.jar:1.3.0.RELEASE]
	at org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter.forward(RibbonRoutingFilter.java:158) ~[spring-cloud-netflix-core-1.3.0.RELEASE.jar:1.3.0.RELEASE]
	at org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter.run(RibbonRoutingFilter.java:106) ~[spring-cloud-netflix-core-1.3.0.RELEASE.jar:1.3.0.RELEASE]
	at com.netflix.zuul.ZuulFilter.runFilter(ZuulFilter.java:112) ~[zuul-core-1.3.0.jar:1.3.0]
	at com.netflix.zuul.FilterProcessor.processZuulFilter(FilterProcessor.java:193) ~[zuul-core-1.3.0.jar:1.3.0]
	at com.netflix.zuul.FilterProcessor.runFilters(FilterProcessor.java:157) ~[zuul-core-1.3.0.jar:1.3.0]

Caused by: org.apache.http.NoHttpResponseException: 192.168.0.101:8090 failed to respond
	at org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:141) ~[httpclient-4.5.3.jar:4.5.3]
	at org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:56) ~[httpclient-4.5.3.jar:4.5.3]
	at org.apache.http.impl.io.AbstractMessageParser.parse(AbstractMessageParser.java:259) ~[httpcore-4.4.6.jar:4.4.6]
	at org.apache.http.impl.DefaultBHttpClientConnection.receiveResponseHeader(DefaultBHttpClientConnection.java:163) ~[httpcore-4.4.6.jar:4.4.6]
	at org.apache.http.impl.conn.CPoolProxy.receiveResponseHeader(CPoolProxy.java:165) ~[httpclient-4.5.3.jar:4.5.3]
	at org.apache.http.protocol.HttpRequestExecutor.doReceiveResponse(HttpRequestExecutor.java:273) ~[httpcore-4.4.6.jar:4.4.6]
```
方案：

#### 超时设置
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds：断路器的超时时间需要大于ribbon的超时时间，不然不会触发重试。

hello-service.ribbon.ConnectTimeout：请求连接的超时时间

hello-service.ribbon.ReadTimeout：请求处理的超时时间

hello-service.ribbon.OkToRetryOnAllOperations：对所有操作请求都进行重试

hello-service.ribbon.MaxAutoRetriesNextServer：切换实例的重试次数

hello-service.ribbon.MaxAutoRetries：对当前实例的重试次数

#### Fegin的post调用
http://www.itmuch.com/spring-cloud-sum/feign-multiple-params/
