# 参考
分布式事务：不过是在一致性、吞吐量和复杂度之间，做一个选择
https://yq.aliyun.com/articles/109238?spm=5176.100240.searchblog.170.ReaRFD

微服务架构下的事务一致性保证
https://yq.aliyun.com/articles/66109?spm=5176.8091938.0.0.betWSt

大话程序猿眼里的高并发
https://yq.aliyun.com/articles/110870?spm=5176.100240.searchblog.192.J8v18g

# 项目
RPO：招聘流程外包英文全称：Recruitment Process Outsourcing

ATS：招聘管理平台Applicant Tracking Systems

# 理论
分布式中 CAP BASE ACID 理解
http://blog.csdn.net/dellme99/article/details/15340955

## CAP
Consistency(一致性), 数据一致更新，所有数据变动都是同步的

Availability(可用性), 好的响应性能

Partition tolerance(分区容错性) 可靠性

定理：任何分布式系统只可同时满足二点，没法三者兼顾。

忠告：架构师不要将精力浪费在如何设计能满足三者的完美分布式系统，而是应该进行取舍

## ACID
Atomicity原子性：一个事务中所有操作都必须全部完成，要么全部不完成。

Consistency一致性. 在事务开始或结束时，数据库应该在一致状态。

Isolation隔离层. 事务将假定只有它自己在操作数据库，彼此不知晓。

Durability. 一旦事务完成，就不能返回。

## BASE 弱一致性协议
基本可用（Basically Available）：系统能够基本运行、一直提供服务。

软状态（Soft-state）：系统不要求一直保持强一致状态。

最终一致性（Eventual consistency）：系统需要在某一时刻后达到一致性要求


# 1.pinniu下载数/充值的逻辑思考（并发下的数据一致性）
讲高吞吐，利用`缓存抗读请求`，利用`水平扩展增加性能是提升吞吐量`的根本方案
#### 1.1 当前逻辑
##### 1.1.1 下载
1. ribbon的请求(循环3次)
2. 获取下载简历，如果有直接返回，否则#3
3. 根据accountId和resumeId插入下载记录表中，如果成功就#4，如果失败就尝试获取下载简历，成功都返回，失败#1
4. 利用CAS来更新下载数UPDATE xxx SET down_coin=down_coin-1 WHERE account_id=? and down_coin>0 and down_coin=down_coin_old
5. 中间出现异常，返回到#1

##### 1.1.2 充值
1. ribbon的请求(循环3次)
2. 根据orderId插入充值记录表中，如果成功就#3，如果失败#1
3. 利用CAS来更新下载数UPDATE xxx SET down_coin=down_coin+? WHERE account_id=? and down_coin=down_coin_old
4. 中间出现异常，返回到#1

#### 1.2 系统流量变大怎么优化
1. 将下载的操作请求放入MQ中
2. MQ的处理逻辑跟上面一样

#### 1.3 继续优化呢？
1. 服务器水平扩展，新增机器看能否解决
2. 如不能解决就数据库实施读写分离

#### 1.4 继续优化，保证高可用？
1. 限流，抛弃部分流量
2. 服务降级，将非核心服务降级，保证核心服务