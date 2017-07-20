
###### Java获取IP地址
```java
private static String getMyIP() throws IOException {
    InputStream ins = null;
    try {
        URL url = new URL("http://iframe.ip138.com/ic.asp");
        URLConnection con = url.openConnection();
        ins = con.getInputStream();
        InputStreamReader isReader = new InputStreamReader(ins, "GB2312");
        BufferedReader bReader = new BufferedReader(isReader);
        StringBuffer webContent = new StringBuffer();
        String str = null;
        while ((str = bReader.readLine()) != null) {
            webContent.append(str);
        }
        int start = webContent.indexOf("[") + 1;
        int end = webContent.indexOf("]");
        return webContent.substring(start, end);
    } finally {
        if (ins != null) {
            ins.close();
        }
    }
}

private static String getMyIPLocal() throws IOException {
    InetAddress ia = InetAddress.getLocalHost();
    return ia.getHostAddress();
}
```
## hashcode
hashcode方法返回该对象的哈希码值。支持该方法是为哈希表提供一些优点，例如，java.util.Hashtable 提供的哈希表。

hashCode 的常规协定是：
在 Java 应用程序执行期间，在同一对象上多次调用 hashCode 方法时，必须一致地返回相同的整数，前提是对象上 equals 比较中所用的信息没有被修改。从某一应用程序的一次执行到同一应用程序的另一次执行，该整数无需保持一致。
如果根据 equals(Object) 方法，两个对象是相等的，那么在两个对象中的每个对象上调用 hashCode 方法都必须生成相同的整数结果。
以下情况不 是必需的：如果根据 equals(java.lang.Object) 方法，两个对象不相等，那么在两个对象中的任一对象上调用 hashCode 方法必定会生成不同的整数结果。但是，程序员应该知道，为不相等的对象生成不同整数结果可以提高哈希表的性能。
实际上，由 Object 类定义的 hashCode 方法确实会针对不同的对象返回不同的整数。（这一般是通过将该对象的内部地址转换成一个整数来实现的，但是 JavaTM 编程语言不需要这种实现技巧。）

当equals方法被重写时，通常有必要重写 hashCode 方法，以维护 hashCode 方法的常规协定，该协定声明相等对象必须具有相等的哈希码。