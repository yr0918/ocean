import java.util.List;

public class Print {

    public static void print(int[] data) {
        for (int d : data) {
            System.out.print(d + ",");
        }
        System.out.println();
    }

    public static void print(List data) {
        for (Object d : data) {
            System.out.print(d + ",");
        }
        System.out.println();
    }
}
