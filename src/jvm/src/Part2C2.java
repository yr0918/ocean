import java.util.ArrayList;
import java.util.List;

public class Part2C2 {
    public static void run() {
//        try {
//            Thread.sleep(60000);
//        } catch (Exception e) {
//        }
//        testOutOfMemoryError();


        int level = 0;
        try {
            level = testStackOverflowError(level);
        } catch (Exception e) {
            System.out.println("level:" + level);
            throw e;
        }
    }

    static class OutMemoryClass {
    }

    /**
     * JVM运行参数：-Xmx20m -Xms20m -XX:+HeapDumpOnOutOfMemoryError
     */
    public static void testOutOfMemoryError() {
        List<OutMemoryClass> objs = new ArrayList<>();
        while (true) {
            objs.add(new OutMemoryClass());
        }
    }

    /**
     * VM -Xss128k
     *
     * @param level
     * @return
     */
    public static int testStackOverflowError(int level) {
        return testStackOverflowError(level++);
    }
}
