
探索jdk8之ConcurrentHashMap 的实现机制
http://www.cnblogs.com/huaizuo/archive/2016/04/20/5413069.html

ConcurrentHashMap源码分析（JDK8版本）
http://blog.csdn.net/u010723709/article/details/48007881

# 核心方法概述

1. CAS算法；unsafe.compareAndSwapInt(this, valueOffset, expect, update);  CAS(Compare And Swap)，意思是如果valueOffset位置包含的值与expect值相同，
    则更新valueOffset位置的值为update，并返回true，否则不更新，返回false。
1. 与Java8的HashMap有相通之处，底层依然由“数组”+链表+红黑树；
1. 底层结构存放的是TreeBin对象，而不是TreeNode对象；
### put方法基本逻辑
```
V putVal(K key, V value, boolean onlyIfAbsent) {
    int hash = spread(key.hashCode());
    //这边加了一个循环，就是不断的尝试，
    //1.table的初始化和casTabAt用到了compareAndSwapInt、compareAndSwapObject，
    //  如果其他线程在修改table，尝试就会失败，所以需要不断尝试
    //2.系统在帮助扩容后，需要继续插入新节点
    //3.除非成功插入，否则不退出循环
    for (Node<K,V>[] tab = table;;) {
        if (tab == null || (n = tab.length) == 0)------------------------------//初始化
            tab = initTable();---------------//CAS无锁方式
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {---------------//asTabAt无锁方式插入节点
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;
        } else if ((fh = f.hash) == MOVED)-----------------------//检查到扩容，帮助其扩容；扩容完根据新的tab再添加
                                                                 //帮助并行copy原数据到新的tab中
            tab = helpTransfer(tab, f);----------------------//如果tab有nextTable节点，调用transfer直接进入复制阶段
        else {
            synchronized (f) {--------------------------------------------//锁住当前头结点
                if (fh >= 0) {-------------------------------------------//链表直接添加到尾部

                }else if (f instanceof TreeBin) {------------------------//红黑树方式添加

                }
                if (binCount >= TREEIFY_THRESHOLD)---------------------//转换为红黑树
            }
        }
    }
    addCount(1L, binCount);-----------------------------//新增数量，检查是否需要扩容sumCount()>this.sizeCtl
}
```
### transfor扩容方法（table的元素数量达到容量阈值sizeCtl）
```java
整个扩容分为两部分：

1.构建一个nextTable，大小为table的两倍

2.把table的数据复制到nextTable中：可以支持节点的并发复制，这样性能自然提升不少

/**
* 一个过渡的table表  只有在扩容的时候才会使用
*/
private transient volatile Node<K,V>[] nextTable;

void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
    ...省略
    if (nextTab == null) {------------------------//sizeCtl>=0时，传的为null，否则传进来的都是this.nextTable
        try {
            Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n << 1];------//构造一个nextTable对象 它的容量是原来的两倍
            nextTab = nt;
        } catch (Throwable ex) {
            sizeCtl = Integer.MAX_VALUE;
            return;
        }
        nextTable = nextTab;
        transferIndex = n;
    }
    ForwardingNode<K,V> fwd = new ForwardingNode<K,V>(nextTab);
    boolean advance = true;
    boolean finishing = false; // to ensure sweep before committing nextTab
    for (int i = 0, bound = 0;;) {
        while (advance) {---------------------------//获取table的i
            int nextIndex, nextBound;
            if (--i >= bound || finishing)
                advance = false;
            // transferIndex = 0表示table中所有数组元素都已经有其他线程负责扩容
            else if ((nextIndex = transferIndex) <= 0) {
                i = -1;
                advance = false;
            }
            // 尝试更新transferIndex，获取当前线程执行扩容复制的索引区间
            // 更新成功，则当前线程负责完成索引为(nextBound，nextIndex)之间的桶首节点扩容
            else if (U.compareAndSwapInt
                     (this, TRANSFERINDEX, nextIndex,
                      nextBound = (nextIndex > stride ?
                                   nextIndex - stride : 0))) {
                bound = nextBound;
                i = nextIndex - 1;
                advance = false;
            }
        }
        if (i < 0 || i >= n || i + n >= nextn) {
            int sc;
            if (finishing) {// a
                nextTable = null;
                table = nextTab;
                // 扩容成功，设置新sizeCtl，仍然为总大小的0.75
                sizeCtl = (n << 1) - (n >>> 1);
                return;
            }
            //利用CAS方法更新这个扩容阈值，在这里面sizectl值减一，说明新加入一个线程参与到扩容操作,参考sizeCtl的注释
            if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
                //如果有多个线程进行扩容，那么这个值在第二个线程以后就不会相等，因为sizeCtl已经被减1了，所以后面的线程就只能直接返回,始终保证只有一个线程执行了 a(上面注释a)
                if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
                    return;
                finishing = advance = true;//finishing和advance保证线程已经扩容完成了可以退出循环
                i = n; // recheck before commit
            }
        }
        else if ((f = tabAt(tab, i)) == null)-----//当前没有节点，通过CAS插入ForwardingNode，用于告诉其它线程该槽位已经处理过了
            advance = casTabAt(tab, i, null, fwd);
        else if ((fh = f.hash) == MOVED)-------//已经被其他线程处理了，该节点的hash值为MOVED(-1)，则直接跳过，继续处理下一个槽位
            advance = true; // already processed
        else {
            synchronized (f) {--------------//单线程将数据从tab拷贝到nextTab中
                if (fh >= 0) {--------------//链表
                    ...省略
                    for (Node<K,V> p = f; p != lastRun; p = p.next) {
                        int ph = p.hash; K pk = p.key; V pv = p.val;
                        if ((ph & n) == 0)
                            ln = new Node<K,V>(ph, pk, pv, ln);
                        else
                            hn = new Node<K,V>(ph, pk, pv, hn);
                    }
                    setTabAt(nextTab, i, ln);--------//hash & n==0 更新到新tab的i桶中
                    setTabAt(nextTab, i + n, hn);----//hash & n!=0 更新到新tab的i+n桶中
                    setTabAt(tab, i, fwd);---------//将原tab标记正在扩容，
                    advance = true;
                } else if (f instanceof TreeBin) {-------//红黑树
                    跟链表的实现逻辑差不多
                    setTabAt(nextTab, i, ln);
                    setTabAt(nextTab, i + n, hn);
                    setTabAt(tab, i, fwd);
                    advance = true;
                }
            }
        }
    }
}

```

![](png/concurrentHashMap.transfer.jpg)


### get方法基本逻辑
```java
    public V get(Object key) {
        Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
        int h = spread(key.hashCode());
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (e = tabAt(tab, (n - 1) & h)) != null) {
            if ((eh = e.hash) == h) {
                if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                    return e.val;
            }
            else if (eh < 0)//如果eh=-1就说明e节点为ForWordingNode,这说明什么，说明这个节点已经不存在了，被另一个线程正则扩容
                            //所以要查找key对应的值的话，直接到新newtable找
                return (p = e.find(h, key)) != null ? p.val : null;
            while ((e = e.next) != null) {
                if (e.hash == h &&
                    ((ek = e.key) == key || (ek != null && key.equals(ek))))
                    return e.val;
            }
        }
        return null;
    }
```


# 内部结构
```java
static final int TREEIFY_THRESHOLD = 8; // 链表转树阀值，大于8时

//树转链表阀值，小于等于6（tranfer时，lc、hc=0两个计数器分别++记录
//原bin、新binTreeNode数量，<=UNTREEIFY_THRESHOLD 则untreeify(lo)）。【仅在扩容tranfer时才可能树转链表】
static final int UNTREEIFY_THRESHOLD = 6;

/**
 * races. Updated via CAS.
 * 记录容器的容量大小，通过CAS更新
 */
private transient volatile long baseCount;

/**
* sizeCtl是控制标识符，不同的值表示不同的意义：
*   -1代表正在初始化
*   -N 表示有N-1个线程正在进行扩容操作
*   0/N 正数或0代表hash表还没有被初始化
*     这个数值表示初始化或下一次进行扩容的大小，类似于扩容阈值。
*     它的值始终是当前ConcurrentHashMap容量的0.75倍，这与loadfactor是对应的。实际容量>=sizeCtl，则扩容。
**/
private transient volatile int sizeCtl;

/**
 *  自旋锁 （锁定通过 CAS） 在调整大小和/或创建 CounterCells 时使用。 在CounterCell类更新value中会使用，功能类似显示锁和内置锁，性能更好
 *  在Striped64类也有应用
 */
private transient volatile int cellsBusy;

//节点类Node，注意val和next是volatile类型
static class Node<K,V> implements Map.Entry<K,V> {
final int hash;
final K key;
volatile V val;//保证可见性
volatile Node<K,V> next;

Node(int hash, K key, V val, Node<K,V> next) {
    this.hash = hash;
    this.key = key;
    this.val = val;
    this.next = next;
}
```
# 方法剖析

## put方法
```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    //这边加了一个循环，就是不断的尝试，因为在table的初始化initTable和casTabAt用到了compareAndSwapInt和compareAndSwapObject
    //因为如果其他线程正在修改tab，那么尝试就会失败，所以这边要加一个for循环，不断的尝试
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {------------------------//标注@1 CAS操作
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))----------//标注@2 CAS操作
                break;
        }
        else if ((fh = f.hash) == MOVED)-----//正在进行transfer操作，返回扩容完成后的table-----//标注@4
            tab = helpTransfer(tab, f);------//检测到正在扩容，则帮助其扩容
        else {
            V oldVal = null;
            //这个地方设计非常的巧妙，内置锁synchronized锁住了f,因为f是指定特定的tab[i]的，
            //所以就锁住了整行链表,这个设计跟分段锁有异曲同工之妙，只是其他读取操作需要用cas来保证
            synchronized (f) {
                if (tabAt(tab, i) == f) {------------------------------------------------//标注@3 CAS操作
                    if (fh >= 0) {
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash && ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent) e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {------------------//链表中直接尾部插入新节点
                                pred.next = new Node<K,V>(hash, key, value, null);
                                break;
                            }
                        }
                    } else if (f instanceof TreeBin) {----------//红黑树实现
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key, value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);------//转化为红黑树
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```
我们看到代码注释中的@1、@2、@3我特定标注的，因为这些操作都是按照CAS的，其中关键部分已经做了注释，要正确取到真实数据需要知道变量所在的内存偏移量
```java
private static final long ABASE;
private static final int ASHIFT;

static {
    try {
        U = sun.misc.Unsafe.getUnsafe();
        Class<?> ak = Node[].class;
        //可以获取数组第一个元素的偏移地址
        ABASE = U.arrayBaseOffset(ak);
        //arrayIndexScale可以获取数组的转换因子，也就是数组中元素的增量地址
        //将arrayBaseOffset与arrayIndexScale配合使用，可以定位数组中每个元素在内存中的位置。
        int scale = U.arrayIndexScale(ak);
        if ((scale & (scale - 1)) != 0)
            throw new Error("data type scale not a power of two");
        ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
    } catch (Exception e) {
        throw new Error(e);
    }
}

@SuppressWarnings("unchecked")
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
    return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
}

/*
 *但是这边为什么i要等于((long)i << ASHIFT) + ABASE呢,计算偏移量
 *ASHIFT是指tab[i]中第i个元素在相对于数组第一个元素的偏移量，而ABASE就算第一数组的内存素的偏移地址
 *所以呢，((long)i << ASHIFT) + ABASE就算i最后的地址
 * 那么compareAndSwapObject的作用就算tab[i]和c比较，如果相等就tab[i]=v否则tab[i]=c;
*/
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i, Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}

static final <K,V> void setTabAt(Node<K,V>[] tab, int i, Node<K,V> v) {
    U.putObjectVolatile(tab, ((long)i << ASHIFT) + ABASE, v);
}
```
标注@4，当(fh = f.hash) == MOVED(-1)，涉及到ForwardingNode，这个类是继承Node类的，他在初始化的时候hash值传了MOVED，ConcurrentHashMap在的数据结构是
Table[]和链表组成，所以如果Table节点是ForwardNode节点的话那么Hash的值就等于-1，在扩容的时候，旧的Table的节点会临时用ForwardNode代替

一个用于连接两个table的节点类。它包含一个nextTable指针，用于指向下一张表。而且这个节点的key value next指针全部为null，它的hash值为-1.
这里面定义的find的方法是从nextTable里进行查询节点，而不是以自身为头节点进行查找
```java
static final class ForwardingNode<K,V> extends Node<K,V> {
    final Node<K,V>[] nextTable;
    ForwardingNode(Node<K,V>[] tab) {
        //MOVED 位-1，说明ForwardNode的节点的hash值为-1
        super(MOVED, null, null, null);
        this.nextTable = tab;
}
```
在标注@4条件下会执行helpTransfer方法，如果线程进入到这边说明已经有其他线程正在做扩容操作，这个是一个辅助方法，
这是一个协助扩容的方法。这个方法被调用的时候，当前ConcurrentHashMap一定已经有了nextTable对象，首先拿到这个nextTable对象，
调用transfer方法。回看上面的transfer方法可以看到，当本线程进入扩容方法的时候会直接进入复制阶段
```java
final Node<K,V>[] helpTransfer(Node<K,V>[] tab, Node<K,V> f) {
    Node<K,V>[] nextTab; int sc;
    if (tab != null && (f instanceof ForwardingNode) &&
        (nextTab = ((ForwardingNode<K,V>)f).nextTable) != null) {
        int rs = resizeStamp(tab.length);
        while (nextTab == nextTable && table == tab && (sc = sizeCtl) < 0) {
            //下面几种情况和addCount的方法一样，请参考addCount的备注
            if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                sc == rs + MAX_RESIZERS || transferIndex <= 0)
                break;
            if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                transfer(tab, nextTab);
                break;
            }
        }
        return nextTab;
    }
    return table;
}
```
接下来是链表转化为红黑树的操作，其实和HashMap是一样的，但是这里涉及到并发，它其实也是通过synchronized和CAS来控制并发的

最后会执行addCount方法
```java
private final void addCount(long x, int check) {
    CounterCell[] as; long b, s;

    //U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x) 每次进来都baseCount都加1因为x=1
    if ((as = counterCells) != null ||
            !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)) {------------//标注@1
        CounterCell a; long v; int m;
        boolean uncontended = true;
        if (as == null || (m = as.length - 1) < 0 ||
            (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
            !(uncontended = U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
            //多线程CAS发生失败的时候执行
            fullAddCount(x, uncontended);------------------------------------------------////标注@2
            return;
        }
        if (check <= 1)
            return;
        s = sumCount();
    }
    if (check >= 0) {
        Node<K,V>[] tab, nt; int n, sc;
        //当条件满足开始扩容
        while (s >= (long)(sc = sizeCtl) && (tab = table) != null &&
               (n = tab.length) < MAXIMUM_CAPACITY) {
            int rs = resizeStamp(n);
            if (sc < 0) {//如果小于0说明已经有线程在进行扩容操作了
                //以下的情况说明已经有在扩容或者多线程进行了扩容，其他线程直接break不要进入扩容操作
                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                    sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
                    transferIndex <= 0)
                    break;
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))//如果相等说明扩容已经完成，可以继续扩容
                    transfer(tab, nt);
            }
            //这个时候sizeCtl已经等于(rs << RESIZE_STAMP_SHIFT) + 2等于一个大的负数，这边加上2很巧妙,因为transfer后面对sizeCtl--操作的时候，最多只能减两次就结束
            else if (U.compareAndSwapInt(this, SIZECTL, sc,
                                         (rs << RESIZE_STAMP_SHIFT) + 2))
                transfer(tab, null);
            s = sumCount();
        }
    }
}
```
看上面addCount方法标注@1，每次都会对baseCount 加1，如果并发竞争太大，那么可能导致U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x) 失败，
那么为了提高高并发的时候baseCount可见性失败的问题，又避免一直重试，这样性能会有很大的影响，那么在jdk8的时候是有引入一个类Striped64，其中
LongAdder和DoubleAdder就是对这个类的实现。这两个方法都是为解决高并发场景而生的，是AtomicLong的加强版，AtomicLong在高并发场景性能会比
LongAdder差。但是LongAdder的空间复杂度会高点
```java
private final void fullAddCount(long x, boolean wasUncontended) {
    int h;
    //获取当前线程的probe值作为hash值,如果0则强制初始化当前线程的Probe值，初始化的probe值不为0
    if ((h = ThreadLocalRandom.getProbe()) == 0) {
        ThreadLocalRandom.localInit();      // force initialization
        h = ThreadLocalRandom.getProbe();
        wasUncontended = true;//设置未竞争标记为true
    }
    boolean collide = false;                // True if last slot nonempty
    for (;;) {
        CounterCell[] as; CounterCell a; int n; long v;
        if ((as = counterCells) != null && (n = as.length) > 0) {
            if ((a = as[(n - 1) & h]) == null) {
                if (cellsBusy == 0) {            // Try to attach new Cell如果当前没有CounterCell就创建一个
                    CounterCell r = new CounterCell(x); // Optimistic create
                    if (cellsBusy == 0 &&
                        U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {//这边加上cellsBusy锁
                        boolean created = false;
                        try {               // Recheck under lock
                            CounterCell[] rs; int m, j;
                            if ((rs = counterCells) != null &&
                                (m = rs.length) > 0 &&
                                rs[j = (m - 1) & h] == null) {
                                rs[j] = r;
                                created = true;
                            }
                        } finally {
                            cellsBusy = 0;//释放cellsBusy锁，让其他线程可以进来
                        }
                        if (created)
                            break;
                        continue;           // Slot is now non-empty
                    }
                }
                collide = false;
            }
            else if (!wasUncontended)       // CAS already known to fail wasUncontended为false说明已经发生了竞争，重置为true重新执行上面代码
                wasUncontended = true;      // Continue after rehash
            else if (U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))//对cell的value值进行累计x（1）
                break;
            else if (counterCells != as || n >= NCPU)
                collide = false;            // At max size or stale 表明as已经过时，说明cells已经初始化完成，看下面，重置collide为false表明已经存在竞争
            else if (!collide)
                collide = true;
            else if (cellsBusy == 0 &&
                     U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                try {
                    if (counterCells == as) {// Expand table unless stale 下面的代码主要是给counterCells扩容，尽可能避免冲突
                        CounterCell[] rs = new CounterCell[n << 1];
                        for (int i = 0; i < n; ++i)
                            rs[i] = as[i];
                        counterCells = rs;
                    }
                } finally {
                    cellsBusy = 0;
                }
                collide = false;
                continue;                   // Retry with expanded table
            }
            h = ThreadLocalRandom.advanceProbe(h);
        }
        else if (cellsBusy == 0 && counterCells == as &&
                 U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {//表明counterCells还没初始化，则初始化，这边用cellsBusy加锁
            boolean init = false;
            try {                           // Initialize table
                if (counterCells == as) {
                    CounterCell[] rs = new CounterCell[2];
                    rs[h & 1] = new CounterCell(x);
                    counterCells = rs;
                    init = true;
                }
            } finally {
                cellsBusy = 0;
            }
            if (init)
                break;
        }
        else if (U.compareAndSwapLong(this, BASECOUNT, v = baseCount, v + x))//最终如果上面的都失败就把x累计到baseCount
            break;                          // Fall back on using base
    }
}
```
源码注释写着See LongAdder version for explanation。我上面已经做了注释了，就不做更多解释了。
回到addCount来，我们每次竟来都对baseCount进行加1当达到一定的容量时，就需要对table进行扩容。
扩容方法就是transfer，这个方法稍微复杂一点，大部分的代码我都做了注释

深入分析ConcurrentHashMap1.8的扩容实现
http://www.importnew.com/23907.html

```java
private final void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
    int n = tab.length, stride;
    if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE)
        // 单个线程允许处理的最少table桶首节点个数
        // 即每个线程的处理任务量
        stride = MIN_TRANSFER_STRIDE; // subdivide range
    if (nextTab == null) {            // initiating
        try {
            @SuppressWarnings("unchecked")
            Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n << 1];
            nextTab = nt;
        } catch (Throwable ex) {      // try to cope with OOME
            sizeCtl = Integer.MAX_VALUE;
            return;
        }
        nextTable = nextTab;
        transferIndex = n;
    }
    int nextn = nextTab.length;
    //构建一个连节点的指针，用于标识位
    ForwardingNode<K,V> fwd = new ForwardingNode<K,V>(nextTab);
    boolean advance = true;
    //循环的关键变量，判断是否已经扩容完成，完成就return，退出循环
    boolean finishing = false; // to ensure sweep before committing nextTab
    for (int i = 0, bound = 0;;) {
        Node<K,V> f; int fh;
        //循环的关键i，i--操作保证了倒序遍历数组
        while (advance) {
            int nextIndex, nextBound;
            if (--i >= bound || finishing)
                advance = false;
            // transferIndex = 0表示table中所有数组元素都已经有其他线程负责扩容
            else if ((nextIndex = transferIndex) <= 0) {//nextIndex=transferIndex=n=tab.length(默认16)
                i = -1;
                advance = false;
            }
            // 尝试更新transferIndex，获取当前线程执行扩容复制的索引区间
            // 更新成功，则当前线程负责完成索引为(nextBound，nextIndex)之间的桶首节点扩容
            else if (U.compareAndSwapInt
                     (this, TRANSFERINDEX, nextIndex,
                      nextBound = (nextIndex > stride ?
                                   nextIndex - stride : 0))) {
                bound = nextBound;
                i = nextIndex - 1;
                advance = false;
            }
        }
        //i<0说明已经遍历完旧的数组tab；i>=n什么时候有可能呢？在下面看到i=n,所以目前i最大应该是n吧。
        //i+n>=nextn,nextn=nextTab.length，所以如果满足i+n>=nextn说明已经扩容完成
        if (i < 0 || i >= n || i + n >= nextn) {
            int sc;
            if (finishing) {// a
                nextTable = null;
                table = nextTab;
                // 扩容成功，设置新sizeCtl，仍然为总大小的0.75
                sizeCtl = (n << 1) - (n >>> 1);
                return;
            }
            //利用CAS方法更新这个扩容阈值，在这里面sizectl值减一，说明新加入一个线程参与到扩容操作,参考sizeCtl的注释
            if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
                //如果有多个线程进行扩容，那么这个值在第二个线程以后就不会相等，因为sizeCtl已经被减1了，所以后面的线程就只能直接返回,始终保证只有一个线程执行了 a(上面注释a)
                if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
                    return;
                finishing = advance = true;//finishing和advance保证线程已经扩容完成了可以退出循环
                i = n; // recheck before commit
            }
        }
        else if ((f = tabAt(tab, i)) == null)//如果tab[i]为null，那么就把fwd插入到tab[i]，表明这个节点已经处理过了
            advance = casTabAt(tab, i, null, fwd);
        else if ((fh = f.hash) == MOVED)//那么如果f.hash=-1的话说明该节点为ForwardingNode，说明该节点已经处理过了
            advance = true; // already processed
        else {
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    Node<K,V> ln, hn;
                    if (fh >= 0) {
                        int runBit = fh & n;
                        Node<K,V> lastRun = f;
                        //这边还对链表进行遍历,这边的的算法和hashmap的算法又不一样了，这班是有点对半拆分的感觉
                        //把链表分表拆分为，hash&n等于0和不等于0的，然后分别放在新表的i和i+n位置
                        //次方法同hashmap的resize
                        for (Node<K,V> p = f.next; p != null; p = p.next) {
                            int b = p.hash & n;
                            if (b != runBit) {
                                runBit = b;
                                lastRun = p;
                            }
                        }
                        if (runBit == 0) {
                            ln = lastRun;
                            hn = null;
                        }
                        else {
                            hn = lastRun;
                            ln = null;
                        }
                        for (Node<K,V> p = f; p != lastRun; p = p.next) {
                            int ph = p.hash; K pk = p.key; V pv = p.val;
                            if ((ph & n) == 0)
                                ln = new Node<K,V>(ph, pk, pv, ln);
                            else
                                hn = new Node<K,V>(ph, pk, pv, hn);
                        }
                        setTabAt(nextTab, i, ln);
                        setTabAt(nextTab, i + n, hn);
                        //把已经替换的节点的旧tab的i的位置用fwd替换，fwd包含nextTab
                        setTabAt(tab, i, fwd);
                        advance = true;
                    }//下面红黑树基本和链表差不多
                    else if (f instanceof TreeBin) {
                        TreeBin<K,V> t = (TreeBin<K,V>)f;
                        TreeNode<K,V> lo = null, loTail = null;
                        TreeNode<K,V> hi = null, hiTail = null;
                        int lc = 0, hc = 0;
                        for (Node<K,V> e = t.first; e != null; e = e.next) {
                            int h = e.hash;
                            TreeNode<K,V> p = new TreeNode<K,V>
                                (h, e.key, e.val, null, null);
                            if ((h & n) == 0) {
                                if ((p.prev = loTail) == null)
                                    lo = p;
                                else
                                    loTail.next = p;
                                loTail = p;
                                ++lc;
                            }
                            else {
                                if ((p.prev = hiTail) == null)
                                    hi = p;
                                else
                                    hiTail.next = p;
                                hiTail = p;
                                ++hc;
                            }
                        }
                        //判断扩容后是否还需要红黑树结构
                        ln = (lc <= UNTREEIFY_THRESHOLD) ? untreeify(lo) :
                            (hc != 0) ? new TreeBin<K,V>(lo) : t;
                        hn = (hc <= UNTREEIFY_THRESHOLD) ? untreeify(hi) :
                            (lc != 0) ? new TreeBin<K,V>(hi) : t;
                        setTabAt(nextTab, i, ln);
                        setTabAt(nextTab, i + n, hn);
                        setTabAt(tab, i, fwd);
                        advance = true;
                    }
                }
            }
        }
    }
}
```

## get方法
```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        // 唯一一处volatile读操作
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0)//如果eh=-1就说明e节点为ForWordingNode,这说明什么，说明这个节点已经不存在了，被另一个线程正则扩容
        //所以要查找key对应的值的话，直接到新newtable找
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```
这个get请求，我们需要cas来保证变量的原子性。如果tab[i]正被锁住，那么CAS就会失败，失败之后就会不断的重试。这也保证了get在高并发情况下不会出错。
我们来分析下到底有多少种情况会导致get在并发的情况下可能取不到值。1、一个线程在get的时候，另一个线程在对同一个key的node进行remove操作；2、一个
线程在get的时候，另一个线程正则重排table。可能导致旧table取不到值。那么本质是，我在get的时候，有其他线程在对同一桶的链表或树进行修改。那么get
是怎么保证同步性的呢？我们看到e = tabAt(tab, (n - 1) & h)) != null，在看下tablAt到底是干嘛的：
```java
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
    return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
}
```
它是对tab[i]进行原子性的读取，因为我们知道putVal等对table的桶操作是有加锁的，那么一般情况下我们对桶的读也是要加锁的，但是我们这边为什么不需要
加锁呢？因为我们用了Unsafe的getObjectVolatile，因为table是volatile类型，所以对tab[i]的原子请求也是可见的。因为如果同步正确的情况下，根据
happens-before原则，对volatile域的写入操作happens-before于每一个后续对同一域的读操作。所以不管其他线程对table链表或树的修改，都对get读取可见