

调用的时候要注意status代码的返回，feign内部会根据不同的代码来解析，如下代码：

统一的错误处理方法，或者业务处理方法
```
SynchronousMethodHandler.java 比较关键，需要阅读
if (response.status() >= 200 && response.status() < 300) {
        if (void.class == metadata.returnType()) {
          return null;
        } else {
          return decode(response);
        }
      } else if (decode404 && response.status() == 404) {
        return decode(response);
      } else {
        throw errorDecoder.decode(metadata.configKey(), response);
      }
```