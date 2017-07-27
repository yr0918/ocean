# Code Style
1. 抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾
1. 如果使用到了设计模式，建议在类名中体现出具体模式
    ```
    说明：将设计模式体现在名字中，有利于阅读者快速理解架构设计思想。
    正例：public class OrderFactory;
    public class LoginProxy;
    public class ResourceObserver;
    ```
1. 接口和实现类的命名有两套规则
    ```
    1）【强制】对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别。
        正例：CacheServiceImpl实现CacheService接口。
    2） 【推荐】 如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able的形式）。
        正例：AbstractTranslator实现 Translatable。
    ```
1. 枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。 说明：枚举其实就是特殊的常量类，且构造方法被默认强制是私有
    ```
    正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKOWN_REASON。
    ```
1. 【参考】各层命名规约：
    ```
    A) Service/DAO层方法命名规约
        1） 获取单个对象的方法用get做前缀。
        2） 获取多个对象的方法用list做前缀。
        3） 获取统计值的方法用count做前缀。
        4） 插入的方法用save（推荐）或insert做前缀。
        5） 删除的方法用remove（推荐）或delete做前缀。
        6） 修改的方法用update做前缀。
    B) 领域模型命名规约
        1） 数据对象：xxxDO，xxx即为数据表名。
        2） 数据传输对象：xxxDTO，xxx为业务领域相关的名称。
        3） 展示对象：xxxVO，xxx一般为网页名称。
        4） POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。
    ```
1. long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解
1. 不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护。如：缓存相关的常量放在类：CacheConsts下；系统配置相关的常量放在类：ConfigConsts
1. 常量的复用层次有五层：跨应用共享常量、应用内共享常量、子工程内共享常量、包内共享常量、类内共享常量
    ```
    1） 跨应用共享常量：放置在二方库中，通常是client.jar中的constant目录下。
    2） 应用内共享常量：放置在一方库的modules中的constant目录下。
        反例：易懂变量也要统一定义成应用内共享常量，两位攻城师在两个类中分别定义了表示“是”的变量：
        类A中：public static final String YES = "yes"; 类B中：public static final String YES = "y";
        A.YES.equals(B.YES)，预期是true，但实际返回为false，导致线上问题。
    3） 子工程内部共享常量：即在当前子工程的constant目录下。
    4） 包内共享常量：即在当前包下单独的constant目录下。
    5） 类内共享常量：直接在类内部private static final定义
    ```
1. 【强制】Object的equals方法容易抛空指针异常，应使用常量或确定有值的对象来调用equals
    ```
    正例： "test".equals(object);
    反例： object.equals("test");
    说明：推荐使用java.util.Objects#equals （JDK7引入的工具类）
    ```
1. 【强制】所有的相同类型的包装类对象之间值的比较，全部使用equals方法比较
    ```
    说明：对于Integer var = ? 在-128至127范围内的赋值，Integer对象是在IntegerCache.cache产生，会复用已有对象，这个区间内的Integer值可以直接
    使用==进行判断，但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑，推荐使用equals方法进行判断。
    ```
1. 关于基本数据类型与包装数据类型的使用标准如下
    ```
    1） 【强制】所有的POJO类属性必须使用包装数据类型。
    2） 【强制】RPC方法的返回值和参数必须使用包装数据类型。
    3） 【推荐】所有的局部变量使用基本数据类型。
    说明：POJO类属性没有初值是提醒使用者在需要使用时，必须自己显式地进行赋值，任何NPE问题，或者入库检查，都由使用者来保证。
    正例：数据库的查询结果可能是null，因为自动拆箱，用基本数据类型接收有NPE风险
    ```
1. 【强制】定义DO/DTO/VO等POJO类时，不要设定任何属性**默认值**
1. 【强制】序列化类新增属性时，请不要修改serialVersionUID字段，避免反序列失败；如果完全不兼容升级，避免反序列化混乱，那么请修改serialVersionUID值
1. 【强制】构造方法里面禁止加入任何业务逻辑，如果有初始化逻辑，请放在init方法中。
1. 【强制】POJO类必须写toString方法。使用IDE的中工具：source> generate toString时，如果继承了另一个POJO类，注意在前面加一下super.toString
1. 【强制】关于hashCode和equals的处理，遵循如下规则：
    ```
    1） 只要重写equals，就必须重写hashCode。
    2） 因为Set存储的是不重复的对象，依据hashCode和equals进行判断，所以Set存储的对象必须重写这两个方法。
    3） 如果自定义对象做为Map的键，那么必须重写hashCode和equals。
    说明：String重写了hashCode和equals方法，所以我们可以非常愉快地使用String对象作为key来使用。
    ```
1. 【强制】 ArrayList的subList结果不可强转成ArrayList，否则会抛出ClassCastException异常：java.util.RandomAccessSubList cannot be cast to java.util.ArrayList
    ```
    说明：subList 返回的是 ArrayList 的内部类 SubList，并不是 ArrayList ，而是 ArrayList 的一个视图，对于SubList子列表的所有操作最终会反映到原列表上。
    ```
1. 【强制】 在subList场景中，高度注意对原集合元素个数的修改，会导致子列表的遍历、增加、删除均产生ConcurrentModificationException 异常
1. 【强制】泛型通配符<? extends T>来接收返回的数据，此写法的泛型集合不能使用add方法，而<? super T>不能使用get方法，做为接口调用赋值时易出错
    ```
    说明：扩展说一下PECS(Producer Extends Consumer Super)原则：
    1）频繁往外读取内容的，适合用上界Extends。
    2）经常往里插入的，适合用下界Super
    ```
1. 【参考】合理利用好集合的有序性(sort)和稳定性(order)，避免集合的无序性(unsort)和不稳定性(unorder)带来的负面影响
    ```
    说明：有序性是指遍历的结果是按某种比较规则依次排列的。稳定性指集合每次遍历的元素次序是一定的。
    如：ArrayList是order/unsort；HashMap是unorder/unsort；TreeSet是order/sort。
    ```
1. 【参考】利用Set元素唯一的特性，可以快速对一个集合进行去重操作，避免使用List的contains方法进行遍历、对比、去重操作
1. 【强制】创建线程或线程池时请指定有意义的线程名称，方便出错时回溯
1. 【强制】SimpleDateFormat 是线程不安全的类，一般不要定义为static变量，如果定义为static，必须加锁，或者使用DateUtils工具类
    ```
    正例：注意线程安全，使用DateUtils。亦推荐如下处理：
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    说明：如果是JDK8的应用，可以使用Instant代替Date，LocalDateTime代替Calendar，DateTimeFormatter代替Simpledateformatter，
    官方给出的解释：simple beautiful strong immutable thread-safe。
    ```
1. 【强制】并发修改同一记录时，避免更新丢失，需要加锁。要么在应用层加锁，要么在缓存加锁，要么在数据库层使用乐观锁，使用version作为更新依据。
    ```
    说明：如果每次访问冲突概率小于20%，推荐使用乐观锁，否则使用悲观锁。乐观锁的重试次数不得小于3次。
    ```
1. 【强制】多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
1. 【推荐】使用CountDownLatch进行异步转同步操作，每个线程退出前必须调用countDown方法，线程执行代码注意catch异常，确保countDown方法可以执行，避免主线程无法执行至await方法，直到超时才返回结果。
1. 【推荐】在并发场景下，通过双重检查锁（double-checked locking）实现延迟初始化的优化问题隐患(可参考 The "Double-Checked Locking is Broken" Declaration)，推荐问题解决方案中较为简单一种（适用于JDK5及以上版本），将目标属性声明为 volatile型
1. 【参考】volatile解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。如果是count++操作，
    使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1); 如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）
1. 【参考】ThreadLocal无法解决共享对象的更新问题，ThreadLocal对象建议使用static修饰。这个变量是针对一个线程内所有操作共有的，所以设置为静态变量，所有此类实例共享此静态变量 ，
    也就是说在类第一次被使用时装载，只分配一块存储空间，所有此类的对象(只要是这个线程内定义的)都可以操控这个变量。
1. 【推荐】除常用方法（如getXxx/isXxx）等外，不要在条件判断中执行其它复杂的语句，将复杂逻辑判断的结果赋值给一个有意义的布尔变量名，以提高可读性。
1. 【参考】下列情形，需要进行参数校验：
    ```
    1） 调用频次低的方法。
    2） 执行时间开销很大的方法。此情形中，参数校验时间几乎可以忽略不计，但如果因为参数错误导致中间执行回退，或者错误，那得不偿失。
    3） 需要极高稳定性和可用性的方法。
    4） 对外提供的开放接口，不管是RPC/API/HTTP接口。
    5） 敏感权限入口。
    ```
1. 【参考】下列情形，不需要进行参数校验：
    ```
    1） 极有可能被循环调用的方法。但在方法说明里必须注明外部参数检查要求。
    2） 底层调用频度比较高的方法。毕竟是像纯净水过滤的最后一道，参数错误不太可能到底层才会暴露问题。
        一般DAO层与Service层都在同一个应用中，部署在同一台服务器中，所以DAO的参数校验，可以省略。
    3） 被声明成private只会被自己代码所调用的方法，如果能够确定调用方法的代码传入参数已经做过检查或者肯定不会有问题，此时可以不校验参数。
    ```
1. 【强制】类、类属性、类方法的注释必须使用Javadoc规范，使用/**内容*/格式，不得使用//xxx方式。
1. 【强制】所有的抽象方法（包括接口中的方法）必须要用Javadoc注释、除了返回值、参数、异常说明外，还必须指出该方法做什么事情，实现什么功能。
1. 【强制】所有的类都必须添加创建者和创建日期。
1. 【强制】方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/* */注释，注意与代码对齐。
1. 【强制】所有的枚举类型字段必须要有注释，说明每个数据项的用途。
1. 【推荐】代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑等的修改。
1. 【参考】特殊注释标记，请注明标记人与标记时间。注意及时处理这些标记，通过标记扫描，经常清理此类标记。线上故障有时候就是来源于这些标记处的代码。
    ```
    1） 待办事宜（TODO）:（ 标记人，标记时间，[预计处理时间]） 表示需要实现，但目前还未实现的功能。这实际上是一个Javadoc的标签，目前的Javadoc还没有实现，
        但已经被广泛使用。只能应用于类，接口和方法（因为它是一个Javadoc标签）。
    2） 错误，不能工作（FIXME）:（标记人，标记时间，[预计处理时间]） 在注释中用FIXME标记某代码是错误的，而且不能工作，需要及时纠正的情况。
    ```
1. 【强制】注意 Math.random() 这个方法返回是double类型，注意取值的范围 0≤x<1（能够取到零值，注意除零异常），如果想获取整数类型的随机数，不要将x放大10的若干倍然后取整，直接使用Random对象的nextInt或者nextLong方法。
1. 【强制】获取当前毫秒数System.currentTimeMillis(); 而不是new Date().getTime(); 说明：如果想获取更加精确的纳秒级时间值，使用System.nanoTime()的方式。在JDK8中，针对统计时间等场景，推荐使用Instant类。
1. 【推荐】任何数据结构的构造或初始化，都应指定大小，避免数据结构无限增长吃光内存。
1. 【推荐】方法的返回值可以为null，不强制返回空集合，或者空对象等，必须添加注释充分说明什么情况下会返回null值。调用方需要进行null判断防止NPE问题
1. 【推荐】定义时区分unchecked / checked 异常，避免直接抛出new RuntimeException()，更不允许抛出Exception或者Throwable，应使用有业务含义的自定义异常。推荐业界已定义过的自定义异常，如：DAOException / ServiceException等。
1. 【参考】在代码中使用“抛异常”还是“返回错误码”，对于公司外的http/api开放接口必须使用“错误码”；而应用内部推荐异常抛出；跨应用间RPC调用优先考虑使用Result方式，封装isSuccess()方法、“错误码”、“错误简短信息”。
    ```
    说明：关于RPC方法返回方式使用Result方式的理由：
    1）使用抛异常返回方式，调用方如果没有捕获到就会产生运行时错误。
    2）如果不加栈信息，只是new自定义异常，加入自己的理解的error message，对于调用端解决问题的帮助不会太多。如果加了栈信息，
        在频繁调用出错的情况下，数据序列化和传输的性能损耗也是问题。
    ```
1.