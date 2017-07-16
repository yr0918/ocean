JVM如何调优
https://yq.aliyun.com/articles/66709?spm=5176.8091938.0.0.iX4fMi

JVM调优总结
https://yq.aliyun.com/articles/66710?spm=5176.8091938.0.0.iX4fMi

JVM调优浅谈
https://yq.aliyun.com/articles/66711?spm=5176.8091938.0.0.iX4fMi

# JVM常用参数表

其一是标准参数（-），所有的JVM实现都必须实现这些参数的功能，而且向后兼容；
其二是非标准参数（-X），默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；
其三是非Stable参数（-XX），此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用；

### 1.1 参数场景案例

# JVM常用工具集

# 2.1 原生工具

# 2.2 优秀集合工具

# 2.3 JAVA程序集成工具

http://developer.51cto.com/art/201507/486162.htm
http://smallvq123.iteye.com/blog/2263370
http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html
年轻代：Eden、Surviovr（From、To）
老年代：
方法区（永久代）：
直接内存：

java -jar -server -Xms3G -Xmx3G -Xss256k -XX:NewSize=1G -XX:MaxNewSize=1G -XX:PermSize=128m
-XX:MaxPermSize=128m -XX:+UseParallelOldGC -XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/home/vmuser/dump -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
-Xloggc:/usr/aaa/dump/heap_trace.txt

年轻代和年老代将根据默认的比例（1：2）

每个线程默认会开启1M的堆栈，用于存放栈帧、调用参数、局部变量等，对大多数应用而言这个默认值太了，一般256K就足用

整个堆大小=年轻代大小 + 年老代大小 + 持久代大小

可以通过下面的参数打Heap Dump信息
-XX:HeapDumpPath
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-Xloggc:/usr/aaa/dump/heap_trace.txt
通过下面参数可以控制OutOfMemoryError时打印堆的信息
-XX:+HeapDumpOnOutOfMemoryError


请看一下一个时间的Java参数配置：（服务器：Linux 64Bit，8Core×16G）
JAVA_OPTS="$JAVA_OPTS -server -Xms3G -Xmx3G -Xss256k -XX:PermSize=128m
-XX:MaxPermSize=128m -XX:+UseParallelOldGC -XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/usr/aaa/dump -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
-Xloggc:/usr/aaa/dump/heap_trace.txt -XX:NewSize=1G -XX:MaxNewSize=1G"
经过观察该配置非常稳定，每次普通GC的时间在10ms左右，Full GC基本不发生，或隔很长很长的时间才发生一次

java -Xms8000m -Xmx8000m -Xmn6000m -Xss256k -XX:PermSize=64m -XX:MaxTenuringThreshold=20
-XX:+CMSScavengeBeforeRemark -XX:CMSInitiatingOccupancyFraction=80 -XX:SurvivorRatio=6
-XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=16 -XX:+UseParNewGC
-XX:+HeapDumpOnOutOfMemoryError -verbose:gc -XX:+PrintGCTimeStamps
-XX:+PrintGCDetails -Xloggc:/home/logs/gc.log