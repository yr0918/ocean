
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