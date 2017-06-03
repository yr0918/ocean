import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner xx = new Scanner( System.in );
        System.out.print("enter a number: ");//println换行；print不换行
        int number = xx.nextInt();//数据类型为int
        int number2 = 2 * number;
        System.out.println( "number = " + number );
        System.out.println( "number2 = " + number2 );

        int[] data = new int[]{12, 98, 100, 28, 299, 39, 10, 28, 40};
        Print.print(data);
//        Algorithm.mergeSort(data, 0, data.length - 1);
//        Algorithm.bubbleSort(data);
//        Algorithm.quickSort(data, 0, data.length - 1);
//        Algorithm.insertSort(data);
//        Algorithm.shellSort(data);
//        Algorithm.selectSort(data);
        Algorithm.heapSort(data);
        System.out.print("after:");
        Print.print(data);
    }
}
