# 基本算法

## 交换排序算法

### 1.冒泡交换排序  O(N2)
`比较相邻的两个元素，大的下沉，小的上浮`
```java
public void bubbleSort(int[] data) {
    if (data == null || data.length == 0) {
        return;
    }

    for (int i = 0; i < data.length; i++) {
        for (int j = 0; j < data.length - i - 1; j++) {//扫描未排好序的data.length-i-1区段
            if (data[j] > data[j + 1]) {
                int temp = data[j];
                data[j] = data[j + 1];
                data[j + 1] = temp;
            }
        }
    }
}
```

### 2.快速交换排序 O(N*logN)
`选择一个基准元素，一次扫描后分成一部分小于基准、一部分大于基准`
```java
public void quickSort(int[] data, int from, int to) {
    if (data == null || data.length == 0) {
        return;
    }
    if (from < to) {
        int fromIndex = from, toIndex = to;
        int midData = data[fromIndex];
        while (fromIndex < toIndex) {
            while (fromIndex < toIndex && data[toIndex] >= midData) {
                toIndex--;
            }
            if (fromIndex < toIndex) {
                data[fromIndex++] = data[toIndex];
            }
            while (fromIndex < toIndex && data[fromIndex] <= midData) {
                fromIndex++;
            }
            if (fromIndex < toIndex) {
                data[toIndex--] = data[fromIndex];
            }
        }
        data[fromIndex] = midData;
        quickSort(data, from, fromIndex - 1);
        quickSort(data, toIndex + 1, to);
    }
}
```
## 选择排序算法


### 1.简单选择排序算法 O(N2)
`扫描选择最小的与第1个元素交换，扫描选择第二小的与第二个元素交换...`
```java
public void selectSort(int[] data) {
    if (data == null || data.length == 0) {
        return;
    }
    for (int i = 0; i < data.length; i++) {
        int position = i;
        for (int j = i + 1; j < data.length; j++) {
            if (data[j] < data[position]) {
                position = j;
            }
        }
        int temp = data[position];
        data[position] = data[i];
        data[i] = temp;
    }
}
```

### 2.堆选择排序算法 O(N*logN)
`构建大顶堆，保证每个父节点（索引小于data.length/2）都大于2个字节点；再逐个交换最大的值`

i结点的父结点下标就为(i–1)/2。它的左右子结点下标分别为2*i+1和2*i+2
```java
public static void heapSort(int[] data) {
    if (data == null || data.length == 0) {
        return;
    }
    //构建大顶堆，其实就是调整顺序,保证每个父节点都大于2个字节点
    for (int i = data.length / 2; i >= 0; i--) {//根据完全二叉树定义<data.length/2都为父节点
        buildHeap(data, i, data.length);
    }

    //逐个交换最大的值
    for (int i = data.length - 1; i > 0; i--) {
        int temp = data[0];
        data[0] = data[i];
        data[i] = temp;
        buildHeap(data, 0, i);
    }
}

private static void buildHeap(int[] data, int parent, int length) {
    int parentValue = data[parent];
    int child = 2 * parent + 1;//左孩子
    while (child < length) {
        //比较右孩子，取值大
        if (child + 1 < length && data[child + 1] > data[child]) {
            child++;
        }
        if (data[child] > parentValue) {
            data[parent] = data[child];
            parent = child;
            child = 2 * child + 1;//继续检索孙子节点是否符合堆的要求
        } else {
            break;
        }
    }
    data[parent] = parentValue;
}
```
## 插入排序排序算法

### 1.直接插入排序算法 O(N2)
`将第n个元素插入到前面已经有序的n-1个元素当做`  ***从第二个元素开始***
```java
public void insertSort(int[] data) {
    if (data == null || data.length == 0) {
        return;
    }
    for (int i = 1; i < data.length; i++) {
        for (int j = i; j > 0; j--) {
            if (data[j - 1] > data[j]) {
                int temp = data[j];
                data[j] = data[j - 1];
                data[j - 1] = temp;
            }
        }
    }
}
```

### 2.希尔插入排序算法 O(N)
`选择一个步长gap=length/2，然后对所对于的gap,gap+gap,gap+gap+gap...元素进行直接插入排序，整个过程直到gap=1结束`

```java
| 13 | 27 | 49 | 55 | 04 | 49 | 38 | 65 | 97 | 26 |
| 1A | .. | 1B | .. | 1C | .. | 1D | .. | 1E | .. |
| .. | 2A | .. | 2B | .. | 2C | .. | 2D | .. | 2E |
// gap=2
// i=gap:       1B与1A
// i=gap+1:   2B与2A
// i=gap+3:   1C与1B,1A
// i=gap+4:   2C与2B,2A
public void shellSort(int[] data) {
    if (data == null || data.length == 0) {
        return;
    }

    for (int gap = data.length / 2; gap > 0; gap /= 2) {//计算步长
        for (int i = gap; i < data.length; i++) {//
            for (int j = i - gap; j >= 0; j -= gap) {
                if (data[j] > data[j + gap]) {
                    int temp = data[j];
                    data[j] = data[j + gap];
                    data[j + gap] = temp;
                }
            }
        }
    }

}
```


## 归并排序算法 O(N*logN)
`将数据分组group=length/2，然后针对分组后的数据进行合并操作(每次合并需要group个新的空间来存放排序后的数据)`

## 

```java
public void mergeSort(int[] data, int from, int to) {
    if (data == null || data.length == 0) {
        return;
    }
    if (from < to) {//
        int mid = (to + from) / 2;
        mergeSort(data, from, mid);//分解排序左边数据
        mergeSort(data, mid + 1, to);//分解排序右边数据

        //合并数据
        int[] mergedData = new int[to - from + 1];//排序后的数据
        int mergeDataIndex = 0;
        int fromIndex = from;//from游标
        int toIndex = mid + 1;//to游标
        while (fromIndex <= mid && toIndex <= to) {
            if (data[fromIndex] <= data[toIndex]) {
                mergedData[mergeDataIndex++] = data[fromIndex++];
            } else {
                mergedData[mergeDataIndex++] = data[toIndex++];
            }
        }
        while (fromIndex < mid) {//填充from中的已排序的数据
            mergedData[mergeDataIndex++] = data[fromIndex++];
        }
        while (toIndex < to) {
            mergedData[mergeDataIndex++] = data[toIndex++];
        }
        System.arraycopy(mergedData, 0, data, from, mergedData.length);
    }
}
```

## 二分查找
```
// 二分查找普通循环实现
public static int binSearch(int srcArray[], int key) {
    int mid = srcArray.length / 2;
    if (key == srcArray[mid]) {
        return mid;
    }

    int start = 0;
    int end = srcArray.length - 1;
    while (start <= end) {
        mid = (end - start) / 2 + start;
        if (key < srcArray[mid]) {
            end = mid - 1;
        } else if (key > srcArray[mid]) {
            start = mid + 1;
        } else {
            return mid;
        }
    }
    return -1;
}

// 二分查找递归实现
public static int binSearch(int srcArray[], int start, int end, int key) {
    int mid = (end - start) / 2 + start;
    if (srcArray[mid] == key) {
        return mid;
    }
    if (start >= end) {
        return -1;
    } else if (key > srcArray[mid]) {
        return binSearch(srcArray, mid + 1, end, key);
    } else if (key < srcArray[mid]) {
        return binSearch(srcArray, start, mid - 1, key);
    }
    return -1;
}
```

## 给定一下号码规则，判断是否是同一个号码
```
 +(86) 13002100000
 130-0210-0000
02113002100000
13002100000
```