# MYSQl
## 注意点
###### 尽量避免SELECT *命令
增加了磁盘需要操作的时间，还是在数据库服务器与WEB服务器是独立分开的情况下
###### 表数据类型选择
1. 能小就用小。表数据类型第一个原则是：使用能正确的表示和存储数据的最短类型。
   这样可以减少对磁盘空间、内存、cpu缓存的使用
2. 避免用NULL，这个也是网上优化技术博文传的最多的一个。理由是额外增加字节，
   还有使索引，索引统计和值更复杂。很多还忽略一个count(列)的问题，count(列)
   是不会统计列值为null的行数
3. 整型、整形优先原则:Tinyint、smallint、mediumint、int、bigint，分别需要
   8、16、24、32、64;能用tinyint的绝不用smallint;int(1) 和int(11)是一样的，
   唯一区别是mysql客户端显示的时候显示多少位;能用整形的不用其他类型替换，如ip可以转换成整形保存

###### 利用LIMIT 1取得唯一行
数据库引擎发现只有1后将停止扫描，而不是去扫描整个表或索引
###### JOIN操作
1. INNER JOIN（内连接,或等值连接）：取得两个表中存在连接匹配关系的记录。
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

 ## 实践

分布式环境下的更新：
https://mp.weixin.qq.com/s/_XlzbmBSj_i-S2PkE5tI_w

分布式环境下的插入：
 http://blog.csdn.net/lululove19870526/article/details/60592981
https://my.oschina.net/mays/blog/809864

 https://mp.weixin.qq.com/s/B9FHHM-NCZvtn7xnawYl1w