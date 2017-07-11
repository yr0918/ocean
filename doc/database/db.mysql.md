# MYSQl

## 注意点
#### 尽量避免SELECT *命令
增加了磁盘需要操作的时间，还是在数据库服务器与WEB服务器是独立分开的情况下
#### 表数据类型选择
1. 能小就用小。表数据类型第一个原则是：使用能正确的表示和存储数据的最短类型。
   这样可以减少对磁盘空间、内存、cpu缓存的使用
2. 避免用NULL，这个也是网上优化技术博文传的最多的一个。理由是额外增加字节，
   还有使索引，索引统计和值更复杂。很多还忽略一个count(列)的问题，count(列)
   是不会统计列值为null的行数
3. 整型、整形优先原则:Tinyint、smallint、mediumint、int、bigint，分别需要
   8、16、24、32、64;能用tinyint的绝不用smallint;int(1) 和int(11)是一样的，
   唯一区别是mysql客户端显示的时候显示多少位;能用整形的不用其他类型替换，如ip可以转换成整形保存

#### 利用LIMIT 1取得唯一行
数据库引擎发现只有1后将停止扫描，而不是去扫描整个表或索引
#### JOIN操作
Mysql Join语法解析与性能分析
http://www.cnblogs.com/BeginMan/p/3754322.html

1. INNER JOIN（内连接,或等值连接）：取得两个表中存在连接匹配关系的记录。
```sql
显示(explicit) inner join
select * from
table a inner join table b
on a.id = b.id;

隐式(implicit) inner join
select a.*, b.*
from table a, table b
where a.id = b.id;
```
2. LEFT JOIN（左连接）：取得左表（table1）完全记录，即是右表（table2）并无对应匹配记录。
3. RIGHT JOIN（右连接）：与 LEFT JOIN 相反，取得右表（table2）完全记录，即是左表（table1）并无匹配对应记录
4. cross join：交叉连接，得到的结果是两个表的乘积（假设集合A={a,b}，集合B={0,1,2}，
    则两个集合的笛卡尔积为{(a,0),(a,1),(a,2),(b,0),(b,1), (b,2)}）
5. 性能：尽量用inner join.避免 LEFT/RIGHT JOIN 和 NULL
6. LEFT/RIGHT注意事项：

 - on与where的执行顺序：在使用Left (right) join的时候，一定要在先给出尽可能多的匹配满足条件，
    减少Where（从匹配阶段产生的数据中检索过滤）的执行
 - 注意ON 子句和 WHERE 子句的不同
 - 尽量避免子查询，而用join
#### union和union all
UNION 用于合并两个或多个 SELECT 语句的结果集，并消去表中任何重复行。
UNION 内部的 SELECT 语句必须拥有相同数量的列，列也必须拥有相似的数据类型。
同时，每条 SELECT 语句中的列的顺序必须相同.
SQL UNION 语法：
sql脚本代码如下:
```sql
SELECT column_name FROM table1
UNION
SELECT column_name FROM table2
```
注释：默认地，UNION 操作符选取不同的值。如果允许重复的值，请使用 UNION ALL。
当 ALL 随 UNION 一起使用时（即 UNION ALL），不消除重复行
SQL UNION ALL 语法
sql脚本代码如下:
```sql
SELECT column_name FROM table1
UNION ALL
SELECT column_name FROM table2
```
## 优化心得
MySQL性能优化总结
http://www.cnblogs.com/luxiaoxun/p/4694144.html

### 原则
#### Query的优化原则：
1. 永远用小结果集驱动大的结果集
2. 尽可能在索引中完成排序
3. 只取出自己需要的Columns
4. 仅仅使用最有效的过滤条件
5. 尽可能避免复杂的Join和子查询

#### Join语句的优化
1. 尽可能减少Join语句中的NestedLoop的循环总次数；“永远用小结果集驱动大的结果集”
2. 优先优化NestedLoop的内层循环；
3. 保证Join语句中被驱动表上Join条件字段已经被索引；
4. 当无法保证被驱动表的Join条件字段被索引且内存资源充足的前提下，不要太吝惜JoinBuffer的设置

#### ORDER BY，GROUP BY和DISTINCT优化
1. ORDER BY的实现与优化：尽可能利用已有的索引来避免实际的排序计算，可以很大幅度的提升ORDER BY操作的性能。优化排序：
    - 加大max_length_for_sort_data参数的设置；
    - 去掉不必要的返回字段；
    - 增大sort_buffer_size参数设置；
1. GROUP BY的实现与优化：由于GROUP BY实际上也同样需要进行排序操作，而且与ORDER BY相比，GROUP BY主要只是多
    了排序之后的分组操作。当然，如果在分组的时候还使用了其他的一些聚合函数，那么还需要一些聚合函数的计算。
    所以，在GROUP BY的实现过程中，与ORDER BY一样也可以利用到索引。
1. DISTINCT的实现与优化：DISTINCT实际上和GROUP BY的操作非常相似，只不过是在GROUP BY之后的每组中只取出一条记
   录而已。所以，DISTINCT的实现和GROUP BY的实现也基本差不多，没有太大的区别。同样可以通过松散索引扫描或者是紧
   凑索引扫描来实现，当然，在无法仅仅使用索引即能完成DISTINCT的时候，MySQL只能通过临时表来完成。但是，和GROUP BY
   有一点差别的是，DISTINCT并不需要进行排序。也就是说，在仅仅只是DISTINCT操作的Query如果无法仅仅利用索引完成操作
   的时候，MySQL会利用临时表来做一次数据的“缓存”，但是不会对临时表中的数据进行filesort操作

### 心得
1. 合理设计数据库结构, 尽量减少磁盘空间 varchar, char,tinyint,int,timestamp,datetime 这些字段该什么时候用, 能用整数表示的字段尽量不要用字符型等等这些
1. 根据业务规则设置合理的索引, 要设置的字段过多, 可以考虑用复合索引, 很多问题都可以通过这种方式解决. 顺便讲了一下复合索引和最左前缀原则.. 拓展话题..
1. 实在数据量大的话, 考虑分库分表分区, 水平切分, 垂直切分都讲了
1. sql 优化方面. 我讲了如何写 sql 避免全表扫描, 比如存在 null 的列不要用索引, 避免用 in, 多用 between and, not exist, 去掉多余的括号, 减少运算,
   去掉重复变量等等. 这个根据自己的积累说就行了, 知识量很大, 不可能说全


## 实践

分布式环境下的更新：
https://mp.weixin.qq.com/s/_XlzbmBSj_i-S2PkE5tI_w

分布式环境下的插入：
 http://blog.csdn.net/lululove19870526/article/details/60592981
https://my.oschina.net/mays/blog/809864

 https://mp.weixin.qq.com/s/B9FHHM-NCZvtn7xnawYl1w