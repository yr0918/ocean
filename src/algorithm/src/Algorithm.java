import java.lang.ref.PhantomReference;

public class Algorithm {
    /**
     * 归并排序：复杂度n*log2n，空间o(1)
     * 将数据分组group=length/2，然后针对分组后的数据进行合并操作(每次合并需要group个新的空间来存放排序后的数据)
     * 实现思路：
     * 1.从中间开始归并
     * 2.递归归并左边，递归归并右边
     * 3.归并操作，生成大小为to-from+1的数组a来存储排序的数据
     * 4.填入a的方式是左右游标比对，小的插入数组中
     * 5.比对完后将多余的自动填入a中
     * 6.把排完序的a合并到原数组中
     * <p>
     * 12,98,100,299,39,10,28,40
     * [12,98],[100,299],[10,39],[28,40]
     * [12,98,100,299],[10,28,39,40]
     * [10,12,28,39,40,98,100,299]
     * from-min-to:sort data
     * 0-0-1:12,98,
     * 2-2-3:100,299,
     * 0-1-3:12,98,100,299,
     * 4-4-5:10,39,
     * 6-6-7:28,40,
     * 4-5-7:10,28,39,40,
     * 0-3-7:10,12,28,39,40,98,100,299,
     */
    public static void mergeSort(int[] data, int from, int to) {
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

    /**
     * 交换排序：冒泡排序 0(n的平方)
     * 比较相邻的两个元素，大的下沉，小的上浮
     *
     * @param data
     */
    public static void bubbleSort(int[] data) {
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

    /**
     * 交互排序:快速排序
     * 选择一个基准元素，一次扫描后分成一部分小于基准、一部分大于基准
     *
     * @param data
     */
    public static void quickSort(int[] data, int from, int to) {
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


    /**
     * 插入排序:直接插入
     * 将第n个元素插入到前面已经有序的n-1个元素当做
     *
     * @param data
     */
    public static void insertSort(int[] data) {
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

    /**
     * 插入排序:希尔排序
     * 选择一个步长gap=length/2，然后对所对于的gap,gap+gap,gap+gap+gap...元素进行直接插入排序，整个过程直到gap=1结束
     *
     * @param data
     */
    public static void shellSort(int[] data) {
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

    /**
     * 选择排序:简单选择
     *
     * @param data
     */
    public static void selectSort(int[] data) {
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

    /**
     * 选择排序：堆排序
     *
     * @param data
     */
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
}
