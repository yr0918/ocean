import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
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

        SearchHighlight s = new SearchHighlight(Arrays.asList("叶溶宝", "金刚葫芦娃", "树叶", "溶宝王","看见那个人看见","我看见你看见"));
        Scanner xx = new Scanner(System.in);
        System.out.println("请输入:");
        String line = xx.nextLine();
        while (!"q".equals(line)) {
            List<String> result = s.search(line);
            Print.print(result);
            System.out.println("请输入:");
            line = xx.nextLine();
        }
    }
}
