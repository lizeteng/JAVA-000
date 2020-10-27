# JVM 核心技术
## 基础知识
### 编程语言
Java 是一种面向对象、静态类型、编译执行、有运行时（VM、GC、类库）、跨平台（二进制）的高级语言。

### 字节码、类加载器、虚拟机
虚拟机通过类加载器加载类字节码至内存中使用。

## 字节码
### 什么是字节码
Java 虚拟机执行的一种指令格式，由单个字节组成，最多支持 256 个操作码。

### 字节码用途
开发人员一般根据字节码来分析两段写法不同、功能相同的代码的差异。

### 字节码类型
根据指令性质，主要分为 4 大类：
* 栈操作指令（aload、astore）
* 程序流程控制指令（ifeq、goto）
* 对象操作指令（invokestatic、invokespecial、invokevirtual、invokeinterface、invokedynamic）
* 算术运算以及类型转换指令（iadd、i2l）

### 字节码生成
编译：`javac xxx.java`
查看字节码：`javap -c xxx.class`

> javap -c 反编译出来的字节码是助记符方式

### 字节码运行时结构
JVM 是一台基于栈的计算机，每个线程都有自己的线程栈，栈由栈帧组成，每一次方法调用，都会创建 1 个栈帧，栈帧由操作数栈、局部变量表、Class 引用（指向当前方法在运行时常量池中对应的 Class）组成。如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk4amafwz5g30bo08omy8.gif)

## 类加载器
### 什么是类加载器
Java 运行时环境的一个部件，负责动态加载 Java 类至 Java 虚拟机内存中。

> 动态加载：一种程序运行机制，能让计算机程序在运行时（非编译时）加载二进制对象至内存中。

### 类生命周期
如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk4an109slj31ca0qsjtj.jpg)
加载（Loading）：通过类加载器从各个来源加载字节码至内存中。
验证（Verification）：保证加载进来的字节码符合虚拟机规范。
准备（Preparation）：为类变量分配内存，赋予初始值。
解析（Resolution）：将常量池内的符号引用替换为直接引用。
初始化（Initialization）：类初始化。
使用（Using）：使用。
卸载（Unloading）：卸载。

### 类加载时机
#### 初始化
* 虚拟机启动时，初始化用户指定的主类（main 方法所在的类）
* new 一个类时，初始化该类
* 调用静态方法时，初始化该静态方法所在的类
* 访问静态字段时，初始化该静态字段所在的类
* 子类初始化会引起父类的初始化
* 接口定义了 default 方法，实现该接口的类初始化会出发该接口的初始化
* 反射调用时，初始化该类
* 初次调用 MethodHandle 时，初始化该 MethodHandle 指向的方法所在的类

#### 不会初始化（可能被加载）
* 子类引用父类的静态字段，只会出发父类的初始化，而不会触发子类的初始化
* 定义对象数组，不会触发该类的初始化
* 常量在编译期间会存入调用类的常量池中，并没有直接引用定义常量的类，不会触发常量类的初始化
* 通过类名获取 Class 对象时，不会触发该类的初始化，例：Hello.class 不会让 Hello 类初始化
* 通过 Class.forName 加载类时，参数 initialize 为 false 时（默认为 true），不会触发该类的初始化
* 通过 ClassLoader 默认的 loadClass 方法，不会触发该类的初始化

### 三类加载器
* 启动类加载器（BootstrapClassLoader）：加载虚拟机依赖的系统类，JVM 底层实现，Java 代码中不可见
* 扩展类加载器（ExtClassLoader）：加载所配置拓展类路径下的类，继承自 URLClassLoader
* 应用类加载器（ApplicationClassLoader）：加载开发者所写的类，继承自 URLClassLoader，开发者自定义的类通常继承自该类

#### 类加载器继承关系
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk4anfrgvaj30um0n20ty.jpg)

#### 类查找顺序
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk4ao0bkzfj30t00s6gn5.jpg)

#### 加载器特点
* 双亲委托：先从父类加载器获取类
* 负责依赖：加载类及该类依赖的类
* 缓存加载：类只会加载一次，缓存下来

> 相同类，被两个类加载器加载出来的不相等

## 内存模型
### 什么是 Java 内存模型（JMM）
Java 虚拟机规范定义了 Java 内存模型（Java Memory Model，JMM），用于屏蔽各种硬件和操作系统的内存访问差异，以实现让 Java 程序在各平台下达到一致的并发效果。

### JVM 内存结构
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk4aoo1vf1j31720u0ter.jpg)

## 启动参数
### 按照形式划分
* -：标准参数，所有 JVM 实现都必须支持这些参数的功能，并且向后兼容
* -D：系统属性
* -X：非标准参数，默认 JVM 实现这些参数的功能，但不保证所有 JVM 都支持，且不向后兼容
* -XX：非稳定参数，各个 JVM 实现会有所不同，将来可能会随时取消

### 按照作用划分
* 系统属性参数
* 运行模式参数
* 堆内存设置参数
* GC 设置参数
* 分析诊断参数
* JavaAgent 参数
