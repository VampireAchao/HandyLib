# HandyLib

#### 介绍
handy的Minecraft快速开发框架 作者 handy(米饭)

#### 项目层级
1. annotation 注解相关包
2. api 一些内部封装
3. command 对命令的封装
4. constants 产量池
5. core 核心工具类
6. db 数据库orm封装
7. exception 异常封装
8. expand 外部扩展类
9. inventory 对gui的封装
10. metrics bstats统计
11. param 参数
12. util 一些常用方法封装  
InitApi 为初始化类

#### 使用方式(本jar已经发布到maven中央仓库)  

[![Maven Central](https://img.shields.io/maven-central/v/cn.handyplus.lib/HandyLib.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22cn.handyplus.lib%22%20AND%20a:%22HandyLib%22)  

步骤 1.将HandyLib添加到您的构建文件  

maven方式
```
<dependency>
  <groupId>cn.handyplus.lib</groupId>
  <artifactId>HandyLib</artifactId>
  <version>3.1.0</version>
</dependency>
```
Gradle方式
```
iimplementation 'cn.handyplus.lib:HandyLib:3.1.0'
```

2. 调用初始化方法 InitApi.getInstance(plugin);

#### 示例项目
[HandyPlugin](https://gitee.com/server-ct/HandyPlugin)

#### JavaDoc
[JavaDoc](https://server-ct.gitee.io/handylib/)