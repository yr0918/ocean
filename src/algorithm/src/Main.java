public class Main {

    public static void main(String[] args) {
        int[] data = new int[]{12, 98, 100, 299, 39, 10, 28, 40};
        System.out.print("before:");
        Print.print(data);
//        Algorithm.mergeSort(data, 0, data.length - 1);
//        Algorithm.bubbleSort(data);
        Algorithm.quickSort(data, 0, data.length - 1);
        System.out.print("after:");
        Print.print(data);
    }
}
