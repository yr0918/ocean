q1.美团网面试回顾： https://hacpai.com/article/1472545562369

q2.美团大众点评面试经验——后台开发：http://blog.csdn.net/sbq63683210/article/details/52903039

q3.美团面试一面+二面+HR面：https://www.nowcoder.com/discuss/24573?type=0&order=0&pos=7&page=1

q4.美团点评：http://blog.csdn.net/u010412719/article/details/52825902

q5.美团-大众点评面经：http://blog.csdn.net/vip_wangsai/article/details/72972936

# 数据库
### q1.innodb 和 myisam的区别
[解答](../doc/database/db.mysql.index.md)
### q1.一张用户表, 找出每个城市有多少人
select count(id) as num from user group by city_id
### q1.删除user表中的重复数据, 只保留一条
delete from user where user_name in (select user_name from user group by user_name having count(user_name)>1) and id not in(select min(id) from user group by user_name having count(user_name)>1)
### q1.数据库优化方面的一些心得
[解答](../doc/database/db.mysql.md)
### q1.当多表关联查询很慢的时候, 有什么办法加速查询..?
这个我想到的办法就是加合理的索引这样子, 面试官说这样数据量上千万的时候还是慢, 他说了一种思路, 进行多次单表查询, 再程序中合并结果. 这种思路我也听过, 但是没说上来, 不知道实际用这种方法情况如何
### q1.排序取前 n 个
select * from user order by create_time desc limit 2,10
### q1.数据库索引相关的提问, 索引原理, 如何设置索引, 怎么看待索引,sql 优化方面的事情
[解答](../doc/database/db.mysql.index.md)
### q2.关于数据库的，基本的ACID，以及联合查找
### q3.1.项目中数据库优化的使用以及原理
### q3.1.数据库索引相关知识（索引原理，涉及B+Tree ，什么时候使用索引），内连接与外连接
### q3.2.数据语句的书写，主要是联合查询
### q4.MySQL 索引 一定要理解为什么使用索引会使查询搜索更快的原理
### 熟悉MySQL数据库应用以及常用性能诊断和优化技术

# 算法
### q1.m1.内存限制 5m, 文件有 50m, 存的都是整数, 让我求出前 10 个最大的数
我的第一反应是堆排序, 一般这种题用堆排序都能解决. 我说了自己的思路. 面试官说是对的, 还有其他办法吗, 我想了好久,
没想到其他办法,一度想起用hashmap,但是一些细节没想好, 大家有啥其他办法吗, 可以说说
### q1.快速排序
### q1.一瓶啤酒 2 元钱,2 个空瓶能换 1 瓶,4 个瓶盖能换一瓶, 现在有 100 元, 我能买到多少瓶啤酒
递归的思路：
```
public static int totalBill(int billCount) {
    if (billCount <= 0) {
        return 0;
    }
    return billCount + totalBill(billCount / 2 + billCount / 4);
}
```
### q2.剑指offer那个之字形打印矩阵的题
### q2.2.一上来就是个top K的问题，然后问如何维护一个堆？接着是一个KMP的问题
TOP K算法(和q1.m1一样)： http://blog.csdn.net/zhanzhan0329/article/details/18902153

Top k问题的讨论（三种方法的java实现及适用范围）: http://www.cnblogs.com/big-sun/p/4085793.html

KMP算法：http://blog.csdn.net/yutianzuijin/article/details/11954939/
### q3.1.基本的算法主要问了二分搜索，快排，二叉树的一道算法题
### q5.“aabbdddaef”,压缩之后的字符串为“a2b2d3a1e1f1”。不可以用高级的数据结构容器
### 两个队列实现栈
### g1.实现X的N次方怎么样更加效率，由于第一次考虑的不多 ，没有想到N是负的情况，另外对于优化也是没有回答好
```
public double myPow(double x, int n) {
    if (x == 0 && n == 0) {
        throw new IllegalArgumentException();
    }
    // 指数正负标记
    boolean isNegative = false;
    // 求n的绝对值
    if (n < 0) {
        n = -n;
        isNegative = true;
    }
    double result = pow(x, n);
    if (isNegative) {
        return 1.0 / result;
    } else {
        return result;
    }
}

public double pow(double x, int n) {
    if (n == 0) {
        return 1;
    } else {
        double result = pow(x, n / 2);
        // n是奇数
        if (n % 2 != 0) {
            return x * result * result;
        } else {
            return result * result;
        }
    }
}
```
### g1.设计树的结构体，实现前序遍历，使用的递归方式写的，然后问非递归怎么写，因为记得不清楚就说使用栈进行 记录的

# JAVA基础
### q1.hashmap原理， 那你觉得为啥不安全, 能详细说下吗？
### q2.HashMap的实现原理（这个问的很多，请重点看，包括各种细节，为何按位与而不是取摸等问题）？HashMap是不是线程安全的？怎么做能使HashMap线程安全（其实考到了并发包的东西）？
### q3.1.主要包括多线程、集合Set以及Map得底层实现原理
[解答](../doc/java.collect/6-HashSet.HashMap.md)
### q1.StringBuilder,StringBuffer的区别
- 两个实现是一样，都是AbstractStringBuilder实现的，采用char[]的方式存储
- StringBuffer采用了synchronized，是线程安全的，并且缓存了toStringCache，使得每次toString不需要重新生成
### q1.ArrayList 的扩容过程
[解答](../doc/java.collect/2-ArrayList.md)
### q2.包括知道哪些集合类？ArrayList和LinkedList有什么区别？
### q2.问了JVM的GC的内容。包括怎么分代，新生代的两种区，以及各用了什么GC算法，新生代怎么才能进入老年代等，以及平时需要调整的一些参数等
### q2.2.问java虚拟机的东西，内存结构？GC等？常用的收集器及其特点？
[解答](../book/深入理解JAVA虚拟机/第二部分.自动内存管理机制.md)
### q2.2.Volatile的作用？Synchronized修饰静态变量和普通变量的区别？
### q3.2.Java中的锁机制以及几种锁的原理以及区别
[解答](../book/深入理解JAVA虚拟机/第五部分.高效并发.md)
### q2.2.面向对象的三个特性（纳尼，哈哈，竟然这么简单）？java线程池的实现原理？
[解答](../doc/java.thread.md)
### q2.3.问到了一些java IO的内容
https://tech.meituan.com/nio.html

### q3.2.Java的接口使用（此处结合抽象类），面向对象的三个特征，多态的理解以及使用
### q4.Collection中常见子类的内部实现，例如HashMap,Hashtable,ArrayList,ConcurrentHashMap等等
### q4.Java多线程和高并发相关，例如线程安全会涉及到synchronized关键字用法（同步块，同步方法，同步静态方法等）和锁机制lock，lock和synchronized和lock的区别
### q4.由上题引申到锁机制的内部实现（独占锁reentrantlock以及共享锁，读写锁）
### q4.线程池原理以及相关类库（ExecuterService及其子类）的具体实现，跟线程池相关的BlockingQueue的具体实现，线程池的拒绝策略等等
### q4.JVM相关:内存模型，分区以及垃圾回收相关
### q4. Java如何实现的夸平台，从.java文件开始分析整个内部执行流程，以及类加载和初始化以及类加载器的原理
### q5.collection的各种容器，各容器之间的关系。ArrayList和LinkedList之间的对比。HashMap和HashTable的一些细节。HashMap的数组中的单链表过长会导致什么问题。HashMap是如何扩容的
### q5.多线程编程的同步问题，几种实现方式。了不了解原子操作？
### q5.多线程的安全问题
### q5.JVM内存的划分，垃圾回收机制

### q5.内部类所使用的外部类中的变量为什么要用final修饰

深入解析String#intern
https://tech.meituan.com/in_depth_understanding_string_intern.html


# 中间件/框架
### q1.Spring boot, 面试官就问这个跟 spring 有什么不同
spring boot 依赖spring，摒弃了繁琐的xml配置改用代码的方式配置，而且很多配置都默认有了，其次spring boot集成了内嵌的tomact，让应用程序能够很简单的已jar方式来运行
### q2.问了Hibernate的优缺点
### q4.对spring中IOC和aop的理解，以及bean的加载机制

# 其他
### q1.了解 http 吗,header 的大小有多大?浏览器缓存在 http header 上有什么体现, 我不要缓存呢?
### HTTP与HTTPS的区别
### HTTP请求报文格式
### g1.问到的是http和https以及原理
### q1.解释浏览器同源策略
### q1.说出前端优化的常见策略
### g1.DNS的工作原理

### q2.网络的基本内容，包括了基本的三次握手、窗口机制、拥塞控制等。这些问题都详细得和他说了一遍
### q2.2.网络的一些基本知识，TCP和UDP区别？五层结构？每层分别有哪些协议？
### q4.计算机网络相关知识，例如tcp和udp的功能和区别以及tcp的三次握手四次挥手（中间出现各种情况的也要了解）以及tcp保证可靠传输的机制有哪些，这些一定要吃透。有一个问题印象比较深刻:类似8080端口属于那一层的概念
### q3.1.计算机网络主要问了三次握手四次挥手
### g1.网络部分，问到四层、7层和5层
### g1.使用的是TCP还是UDP，以及TCP和UDP的区别

### q1.线上问题排查
### q3.2.Linux基本操作
### q3.2.hash一致性的原理





# 求职者总结
### q1.
- 前期的准备很重要, 为了面试我也准备了好久, 目前为止的面试都很顺利, 成功率很高. 来北京面试了四家, 三家都已经拿到 offer 了.
- 多提高自己的语言表达能力, 多主动的表达一些自己的东西, 不要每次都让面试官挖掘
- 面试是实力加运气, 我感觉我这次运气比较好. 问题也没有特别难
- 平时多学习, 努力的人运气不会太差
- 一定要相信自己, 坚持, 算法想不出来的时候再加把劲, 说不定就想出来了, 就算想不出来完整的, 把自己的思考过程表达出来, 思路清晰一点, 而不是只是说我不会, 这样面试官也没法接招.
- 数据库索引, 主从复制, 事务, 优化,jvm 调优, 感觉这些事必问的了. 需要面试的同学可以专项准备一下, 我都是每个专题专项复习的. 都记了博客, 方便查询

### q3.
个人感觉面试官注重基础知识，然后对数据库的使用也比较看重，还有就是算法，特别是二叉树和链表这方面的，还有就是并发这方面的知识，
写得有些乱，见谅哈。然后最近建了一个群，主要用于美团面试的经验交流，以及offer相关的进程，如果大家准备在望京这边租房子的话也
可以加群，互相分享信息。希望大家入群聊一聊，因为美团好像集中在4月份进行面试。