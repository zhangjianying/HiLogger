# HiLogger
简单.美观.实用的日志打印工具类
Simple, pretty and powerful logger for XXXX

### 使用步骤 (Setup)

#### 下载源码 (Download)

```
复制项目src目录下  copy to your project src
```

####  初始化 (Initialize)

​		在AbilityPackage中onInitialize()方法里面初始化

```java
HiLogger.addLogAdapter(new HMOSLogAdapter(0x333) {
     @Override  //debug模式下输出日志
     public boolean isLoggable(int priority, String tag) {
           return BuildConfig.DEBUG;
     }
});
```

更高级一点的用法,可以初始化输出格式
```java
 FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
.showThreadInfo(false)
.methodCount(0)
.tag("MyTag")
.build();

HiLogger.addLogAdapter(new HMOSLogAdapter(formatStrategy,0x333) {
     @Override  //debug模式下输出日志
     public boolean isLoggable(int priority, String tag) {
           return BuildConfig.DEBUG;
     }
});
```


####  使用 (And use)
在需要打印日志的地方
```java
HiLogger.d("hello");
HiLogger.i("hello");
HiLogger.w("hello");
HiLogger.e("hello");


or 

import static com.talkweb.logger.HiLogger.*;
 d("应用未被授予权限");
 i("应用未被授予权限");
 w("应用未被授予权限");
 e("应用未被授予权限");
```

### 输出效果 (Output)

![image-20201027155155871](README.assets/image-20201027155155871.png)



### 更多可选API (Options)

#### 字符串参数  (String format arguments are supported)

```java
HiLogger.d("hello %s", "world");
```

#### 常见集合对象  (Collections are supported)

```java
HiLogger.d(MAP);
HiLogger.d(SET);
HiLogger.d(LIST);
HiLogger.d(ARRAY);

example:
HiLogger.d(Arrays.asList("foo", "bar"));
```


####  JSON对象  (Json  support )
```java
HiLogger.json("{ \"key\": 3, \"value\": \"something\"}");
HiLogger.json("[{ \"key\": 3, \"value\": \"something\"}]");
```