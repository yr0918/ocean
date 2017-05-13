public class Algorithm {
    /**
     * 归并排序：复杂度n*log2n，空间o(1)
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
            mergeSortMergeData(data, from, mid, to);//合并数据
        }
    }

    private static void mergeSortMergeData(int[] data, int from, int mid, int to) {
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
//        System.out.print(from + "-" + mid + "-" + to + ":");
//        print(mergedData);
        System.arraycopy(mergedData, 0, data, from, mergedData.length);
//        mergeDataIndex = 0;
//        while (mergeDataIndex < mergedData.length) {
//            data[from++] = mergedData[mergeDataIndex++];
//        }
    }

    /**
     * 交换排序：冒泡排序 0(n的平方)
     *
     * @param data
     */
    public static void bubbleSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 快速排序
     *
     * @param data
     */
    public static void quickSort(int[] data, int from, int to) {
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

    public static void quickSort1(int[] targetArr, int start, int end) {
        if (start < end) {
            int i = start, j = end;
            int key = targetArr[start];
            while (i < j) {
/*按j--方向遍历目标数组，直到比key小的值为止*/
                while (j > i && targetArr[j] >= key) {
                    j--;
                }
                if (i < j) {
/*targetArr[i]已经保存在key中，可将后面的数填入*/
                    targetArr[i] = targetArr[j];
                    i++;
                }
/*按i++方向遍历目标数组，直到比key大的值为止*/
                while (i < j && targetArr[i] <= key)
/*此处一定要小于等于零，假设数组之内有一亿个1，0交替出现的话，而key的值又恰巧是1的话，那么这个小于等于的作用就会使下面的if语句少执行一亿次。*/ {
                    i++;
                }
                if (i < j) {
/*targetArr[j]已保存在targetArr[i]中，可将前面的值填入*/
                    targetArr[j] = targetArr[i];
                    j--;
                }
            }
/*此时i==j*/
            targetArr[i] = key;

/*递归调用，把key前面的完成排序*/
            quickSort1(targetArr, start, i - 1);


/*递归调用，把key后面的完成排序*/
            quickSort1(targetArr, j + 1, end);
        }

    }
}
