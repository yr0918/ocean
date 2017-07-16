# 1.AOP（切面）
Spring AOP 代理有 CglibAopProxy 和 JdkDynamicAopProxy 两种，图 1 是以 CglibAopProxy 为例，对于 CglibAopProxy，需要调用其内部类的 DynamicAdvisedInterceptor 的 intercept 方法。对于 JdkDynamicAopProxy，需要调用其 invoke 方法


Aspect Oriented Programming的缩写，意思是面向切面编程.与OOP(Object Oriented Programming)面向对象编程对等,都是一种编程思想

从OOP角度分析,我们关注业务的处理逻辑,是属于纵向的行为,从AOP角度分析,我们关注对象行为发生时的问题，是属于横向的问题

AOP就是把贯穿在各个模块之间相同的功能抽取出来，然后封装成一个面

**实现AOP的技术（两大类）**

1. 采用动态代理技术，利用截取消息的方式，对该消息进行装饰，以取代原有对象行为的执行
2. 采用静态织入的方式，引入特定的语法创建“方面”，从而使得编译器可以在编译期间织入有关“方面”的代码

**AOP使用场景**，来封装横切关注点，具体可以在下面的场景中使用:

Spring AOP原理
http://blog.csdn.net/u010723709/article/details/47839307

1. Authentication 权限
1. Caching 缓存
1. Context passing 内容传递
1. Error handling 错误处理
1. Lazy loading　懒加载
1. Debugging　　调试
1. logging, tracing, profiling and monitoring　记录跟踪　优化　校准
1. Performance optimization　性能优化
1. Persistence　　持久化
1. Resource pooling　资源池
1. Synchronization　同步
1. Transactions 事务

Spring默认采取的动态代理机制实现AOP，当动态代理不可用时（代理类无接口）会使用CGlib机制

`2.IOC和DI` （解耦合）Inversion of Control 控制反转，也叫（Dependency Injection）依赖注入

控制权有对象本身专享容器,由容器根据配置文件去创建实例,并创建各个实例之间的关系,则通俗的说，对象的创建再也不需要程序员来管理,而是可以有spring容器来进行创建和销毁,我们只需要关注业务逻辑.

依赖IOC容器并管理bean,有两种,一种是BeanFactory,另一种是ApplicationContext,但是APPlicationContext extends BeanFactory.

核心:Spring中,bean工厂创建的各个实例称作bean

# 事务

透彻的掌握 Spring 中@transactional 的使用
https://www.ibm.com/developerworks/cn/java/j-master-spring-transactional-use/index.html

