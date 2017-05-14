## 线程
现实方式：
1.  集成Thread，实现run方法
2. 实现接口Runnable，然后通过Thread启动

### 1. 线程

#### 线程状态

![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.status.png)

1. start:现成启动方法
2. sleep:在指定的毫秒数内让当前正在执行的线程休眠（暂停执行），此操作受到系统计时器和调度程序精度和准确性的影响不能改变对象的机锁
3. yield:停止当前线程，让同等优先权的线程运行。如果没有同等优先权的线程，那么Yield()方法将不会起作用
4. stop/interrupt:使当前线程停下来等待，直至另一个调用join方法的线程终止
5. join:(通过wait实现) thread.Join把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程。比如在线程B中调用了线程A的Join()方法，直到线程A执行完毕后，才会继续执行线程B
t.join();      //调用join方法，等待线程t执行完毕
t.join(1000);  //等待 t 线程，等待时间是1000毫秒

#### ThreadLocal
用处：保存线程的独立变量。对一个线程类（继承自Thread)
当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。常用于用户登录控制，如记录session信息。

实现：每个Thread都持有一个TreadLocalMap类型的变量（该类是一个轻量级的Map，功能与map一样，区别是桶里放的是entry而不是entry的链表。功能还是一个map。）以本身为key，以目标为value。
主要方法是get()和set(T a)，set之后在map里维护一个threadLocal -> a，get时将a返回。ThreadLocal是一个特殊的容器

### 2. 多线程

#### 2.1 volatile变量
1. `保证线程之间变量的可见性`
2. `禁止指令重排序`
3. 不能保证程序的原子性/一定程度保证有序性
4. 场景：多线程的状态标记量；单实例的double check

###### 2.1.1 多线程的内存模型

1.  原子性：即一个操作或者多个操作 要么全部执行并且执行的过程不会被任何因素打断，要么就都不执行
2. 可见性：可见性是指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值
3. 有序性：即程序执行的顺序按照代码的先后顺序执行；happens-before规则

![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.memory.1.png)
![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.memory.2.png)

#### 2.2 线程同步
两种方式实现现成同步synchronized和Lock；使用规则可定时的、可轮询的与可中断的锁获取操作，公平队列，已经非块结构的锁使用Lock，否则优先使用synchronized

###### 2.2.1 synchronized（块、方法）

###### 2.2.2 Lock锁(公平锁/非公平锁[可插队])AbstractQueuedSynchronizer

ReentrantLock(重入锁)：
 - lock:获取锁，如果锁无法获取，那么当前的线程就变为不可被调度，直到锁被获取到
 - tryLock:如果调用的时候能够获取锁，那么就获取锁并且返回true，如果当前的锁无法获取到，那么这个方法会立刻返回false
 - unlock:释放当前线程占用的锁
 - newCondition:返回一个与当前的锁关联的条件变量。在使用这个条件变量之前，当前线程必须占用锁。调用Condition的await方法，会在等待之前原子地释放锁，并在等待被唤醒后原子的获取锁

`实现：`

初始化时， state=0，表示无人抢占了打水权。这时候，村民A来打水(A线程请求锁)，占了打水权，把state+1，如下所示：

![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.reentrantLock1.png)

线程A取得了锁，把 state原子性+1,这时候state被改为1，A线程继续执行其他任务，然后来了村民B也想打水（线程B请求锁），线程B无法获取锁，生成节点进行排队，如下图所示：

![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.reentrantLock2.png)

初始化的时候，会生成一个空的头节点，然后才是B线程节点，这时候，如果线程A又请求锁，是否需要排队？答案当然是否定的，否则就直接死锁了。当A再次请求锁，就相当于是打水期间，同一家人也来打水了，是有特权的，这时候的状态如下图所示：

![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.reentrantLock3.png)

到了这里，相信大家应该明白了什么是可重入锁了吧。就是一个线程在获取了锁之后，再次去获取了同一个锁，这时候仅仅是把状态值进行累加。如果线程A释放了一次锁，就成这样了：

![](https://github.com/yr0918/ocean/raw/master/doc/img/java.thread.reentrantLock4.png)

仅仅是把状态值减了，只有线程A把此锁全部释放了，状态值减到0了，其他线程才有机会获取锁。当A把锁完全释放后，state恢复为0，然后会通知队列唤醒B线程节点，使B可以再次竞争锁。当然，如果B线程后面还有C线程，C线程继续休眠，除非B执行完了，通知了C线程。注意，当一个线程节点被唤醒然后取得了锁，对应节点会从队列中删除。

ReentrantReadWriteLock：
读写锁 ReadWriteLock读写锁维护了一对相关的锁，一个用于只读操作，一个用于写入操作。只要没有writer，读取锁可以由多个reader线程同时保持。写入锁是独占的

StampedLock：该类是一个读写锁的改进，它的思想是读写锁中读不仅不阻塞读，同时也不应该阻塞写。
读不阻塞写的实现思路：在读的时候如果发生了写，则应当重读而不是在读的时候直接阻塞写

###### 2.2.3 同步器
`CountDownLatch：` 能够使一个线程等待其他线程完成各自的工作后再执行。例如，应用程序的主线程希望在负责启动框架服务的线程已经启动所有的框架服务之后再执行

通过一个计数器来实现的，计数器的初始值为线程的数量。每当一个线程完成了自己的任务后，计数器的值就会减1。当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务

使用场景：
  - 开始执行前等待n个线程完成各自任务：例如应用程序启动类要确保在处理用户请求前，所有N个外部系统已经启动和运行了。
  - 死锁检测：一个非常方便的使用场景是，你可以使用n个线程访问共享资源，在每次测试阶段的线程数目是不同的，并尝试产生死锁

`Semaphore：`可以很轻松完成信号量控制，Semaphore可以控制某个资源可被同时访问的个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。比如在Windows下可以设置共享文件的最大客户端访问个数

Semaphore维护了当前访问的个数，提供同步机制，控制同时访问的个数。在数据结构中链表可以保存“无限”的节点，用Semaphore可以实现有限大小的链表。另外重入锁 ReentrantLock 也可以实现该功能，但实现上要复杂些

`CyclicBarrier：`一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时 CyclicBarrier 很有用。因为该 barrier 在释放等待线程后可以重用，所以称它为循环 的 barrier

使用场景：
需要所有的子任务都完成时，才执行主任务，这个时候就可以选择使用CyclicBarrier。

`Phaser`：涵盖CountDownLatch和CyclicBarrier的功能

`Exchanger：`可以在两个线程之间交换数据，只能是2个线程，他不支持更多的线程之间互换数据。

当线程A调用Exchange对象的exchange()方法后，他会陷入阻塞状态，直到线程B也调用了exchange()方法，然后以线程安全的方式交换数据，之后线程A和B继续运行


#### 2.3 线程池（管理类）

#### 2.4 线程安全集合（容器类）

1. ConcurrentMap
   - ConcurrentHashMap
   - ConcurrentSkipListMap
2. List
   - CopyOnWriteArrayList
   - CopyOnWriteArraySet
3. Queue
   - ArrayBlockingQueue
   - LinkedBlockingQueue

#### 2.5 Atomic原子操作

`通过volatile和CAS(compare and swap)实现`

Atomic包是java.util.concurrent下的另一个专门为线程安全设计的Java包，包含多个原子操作
类。这个包里面提供了一组原子变量类。其基本的特性就是在多线程环境下，当有多个线程
同时执行这些类的实例包含的方法时，具有排他性，即当某个线程进入方法，执行其中的指
令时，不会被其他线程打断，而别的线程就像自旋锁一样，一直等到该方法执行完成，才由J
VM从等待队列中选择一个另一个线程进入，这只是一种逻辑上的理解。实际上是借助硬件的
相关指令来实现的，不会阻塞线程(或者说只是在硬件级别上阻塞了)。可以对基本数据、数组
中的基本数据、对类中的基本数据进行操作。原子变量类相当于一种泛化的volatile变量，能
够支持原子的和有条件的读-改-写操作