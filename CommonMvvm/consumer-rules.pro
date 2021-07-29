#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

-dontskipnonpubliclibraryclassmembers

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

 #优化  不优化输入的类文件
-dontoptimize

#避免混淆泛型
-keepattributes Singature

#保护注解
-keepattributes *Annotation

-keepattributes *Annotation*
-keep class com.lidroid.** { *; }
-keep class * extends java.lang.annotation.Annotation { *; }
