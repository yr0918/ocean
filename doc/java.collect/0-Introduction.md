# Java Collections Framework Internals

# Introduction

本部分内容大部分是引用 [CarpenterLee](http://www.cnblogs.com/CarpenterLee/) ,有部分做了修改，并且新增了Concurrent下集合的内容

# 前提知识

1. [基本数据结构](https://github.com/yr0918/ocean/blob/master/doc/java.collect/java.collect.datastructure.md) 基本的数据结构链表、栈、队列、树、图等的介绍。
2. [常用算法](https://github.com/yr0918/ocean/blob/master/doc/java.collect/java.collect.algorithm.md) 常用算法介绍，比如选择、插入、冒泡、希尔、堆、快速、归并等排序。

# Contents

具体内容安排如下：

1. [Overview](https://github.com/yr0918/ocean/blob/master/doc/java.collect/1-Overview.md) 对Java Collections Framework，以及Java语言特性做出基本介绍。
2. [ArrayList](https://github.com/yr0918/ocean/blob/master/doc/java.collect/2-ArrayList.md) 结合源码对*ArrayList*进行讲解。
3. [LinkedList](https://github.com/yr0918/ocean/blob/master/doc/java.collect/3-LinkedList.md) 结合源码对*LinkedList*进行讲解。
4. [Stack and Queue](https://github.com/yr0918/ocean/blob/master/doc/java.collect/4-Stack%20and%20Queue.md) 以*AarryDeque*为例讲解*Stack*和*Queue*。
5. [TreeSet and TreeMap](https://github.com/yr0918/ocean/blob/master/doc/java.collect/5-TreeSet%20and%20TreeMap.md) 结合源码对*TreeSet*和*TreeMap*进行讲解。
6. [HashSet and HashMap](https://github.com/yr0918/ocean/blob/master/doc/java.collect/6-HashSet%20and%20HashMap.md) 结合源码对*HashSet*和*HashMap*进行讲解。
7. [LinkedHashSet and LinkedHashMap](https://github.com/yr0918/ocean/blob/master/doc/java.collect/7-LinkedHashSet%20and%20LinkedHashMap.md) 结合源码对*LinkedHashSet*和*LinkedHashMap*进行讲解。
8. [PriorityQueue](https://github.com/yr0918/ocean/blob/master/doc/java.collect/8-PriorityQueue.md) 结合源码对*PriorityQueue*进行讲解。
9. [WeakHashMap](https://github.com/yr0918/ocean/blob/master/doc/java.collect/9-WeakHashMap.md) 对*WeakHashMap*做出基本介绍。
10. `待完成`[BlockingDeque](https://github.com/yr0918/ocean/blob/master/doc/java.collect/java.concurrent.BlockingDeque.md) 对\**Deque*做出基本介绍。
11. `待完成`[CopyOnWrite](https://github.com/yr0918/ocean/blob/master/doc/java.collect/java.concurrent.CopyOnWrite.md) 对*CopyOnWrite*\*做出基本介绍。
12. `待完成`[ConcurrentMap](https://github.com/yr0918/ocean/blob/master/doc/java.collect/java.concurrent.ConcurrentMap.md) 对*ConcurrentMap*做出基本介绍。
