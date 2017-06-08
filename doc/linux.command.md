## top


```
>top
top - 11:00:13 up 193 days, 22:31,  1 user,  load average: 0.00, 0.00, 0.00
Tasks:  72 total,   1 running,  71 sleeping,   0 stopped,   0 zombie
Cpu(s):  0.7%us,  0.3%sy,  0.0%ni, 99.0%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:  2003.438M total, 1889.758M used,  113.680M free,  133.559M buffers
Swap:    0.000k total,    0.000k used,    0.000k free,  489.176M cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND
28462 ec2-user  20   0 2584m 415m  16m S  0.7 20.8  35:41.35 java
28290 ec2-user  20   0 2676m 558m  21m S  0.3 27.9  76:52.60 java
    1 root      20   0 19636 2440 2124 S  0.0  0.1   0:13.81 init
    2 root      20   0     0    0    0 S  0.0  0.0   0:00.00 kthreadd
    3 root      20   0     0    0    0 S  0.0  0.0   0:42.23 ksoftirqd/0
    4 root      20   0     0    0    0 S  0.0  0.0   0:00.00 kworker/0:0
    5 root       0 -20     0    0    0 S  0.0  0.0   0:00.00 kworker/0:0H
    7 root      20   0     0    0    0 S  0.0  0.0   5:29.55 rcu_sched
    8 root      20   0     0    0    0 S  0.0  0.0   0:00.00 rcu_bh
```


01:06:48	当前时间
up 1:22	系统运行时间，格式为时:分
1 user	当前登录用户数
load average: 0.06, 0.60, 0.48	系统负载，即任务队列的平均长度。
三个数值分别为 1分钟、5分钟、15分钟前到现在的平均值。
注意：这三个值可以用来判定系统是否负载过高——如果值
持续大于系统 cpu 个数，就需要优化你的程序或者架构了

Tasks: 72 total	进程总数
1 running	正在运行的进程数
71 sleeping	睡眠的进程数
0 stopped	停止的进程数
0 zombie	僵尸进程数

Cpu(s): 0.7% us	用户空间占用CPU百分比
0.3% sy	内核空间占用CPU百分比
0.0% ni	用户进程空间内改变过优先级的进程占用CPU百分比
99.0% id	空闲CPU百分比
0.0% wa	等待输入输出的CPU时间百分比
0.0% hi	Hardware IRQ
0.0% si	Software IRQ

Mem: 2003.438M total	物理内存总量
1889.758M used	使用的物理内存总量
113.680M free	空闲内存总量
133.559M buffers	用作内核缓存的内存量

Swap: 0.000k total	交换区总量
0.000k used	使用的交换区总量
0.000k free	空闲交换区总量
489.176M cached	缓冲的交换区总量：内存中的内容被换出到交换区，而后又被换入到内存，但使用过的交换区尚未被覆盖，该数值即为这些内容已存在于内存中的交换区的大小。相应的内存再次被换出时可不必再对交换区写入。

| 序号 | 列名 | 含义 |
| -------- | -------- | -------- |
|a|	PID|	进程id|
|b|	PPID|	父进程id|
|c|	RUSER|	Real user name|
|d|	UID|	进程所有者的用户id|
|e|	USER|	进程所有者的用户名|
|f|	GROUP|	进程所有者的组名|
|g|	TTY|	启动进程的终端名。不是从终端启动的进程则显示为 ?|
|h|	PR|	优先级|
|i|	NI|	nice值。负值表示高优先级，正值表示低优先级|
|j|	P|	最后使用的CPU，仅在多CPU环境下有意义|
|k|	%CPU|	上次更新到现在的CPU时间占用百分比|
|l|	TIME|	进程使用的CPU时间总计，单位秒|
|m|	TIME+|	进程使用的CPU时间总计，单位1/100秒|
|n|	%MEM|	进程使用的物理内存百分比|
|o|	VIRT|	进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES|
|p|	SWAP|	进程使用的虚拟内存中，被换出的大小，单位kb。|
|q|	RES|	进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA|
|r|	CODE|	可执行代码占用的物理内存大小，单位kb|
|s|	DATA|	可执行代码以外的部分(数据段+栈)占用的物理内存大小，单位kb|
|t|	SHR|	共享内存大小，单位kb|
|u|	nFLT|	页面错误次数|
|v|	nDRT|	最后一次写入到现在，被修改过的页面数。|
|w|	S|	进程状态。D=不可中断的睡眠状态R=运行S=睡眠T=跟踪/停止Z=僵尸进程|
|x|	COMMAND|	命令名/命令行|
|y|	WCHAN|	若该进程在睡眠，则显示睡眠中的系统函数名|
|z|	Flags|	任务标志，参考 sched.h|