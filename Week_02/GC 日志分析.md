# GC 日志分析
## 启动参数
### 日志相关
* -XX:+PrintGC：开启 GC 日志
* -XX:+PrintGCDetails：开启 GC 详细日志
* -XX:+PrintGCDateStamps：开启 GC 日志时间戳
* -Xloggc:xxx.log：GC 日志文件路径

### GC 相关
* -XX:+UseSerialGC：使用串行 GC
* -XX:+UseParallelGC：使用并行 GC，同 -XX:+UseParallelOldGC、-XX:+UseParallelGC -XX:+UseParallelOldGC 等价
* -XX:+UseConcMarkSweepGC：使用 CMS GC，同 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC 等价，如果仅使用 -XX:+UseParNewGC，则老年代 GC 会使用 SerialGC。
* -XX:+UseG1GC：使用 G1 GC，原则上不能指定 G1 的年轻代大小，由 G1 动态调整。

> 如果使用不支持的 GC 组合，会启动失败。

## 事件类型
### Minor GC（小型 GC）
清理年轻代的 GC 事件，又称年轻代 GC（Young GC，简称 YGC）。

### Major GC（大型 GC）
清理老年代的 GC 事件。

### Full GC（完全 GC）
清理整个堆的 GC 事件，包括年轻代和老年代。

## 日志分析
### Serial GC
#### 启动命令
`java -XX:+UseSerialGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

#### Minor GC
`2020-10-28T15:10:24.779-0800: [GC (Allocation Failure) 2020-10-28T15:10:24.779-0800: [DefNew: 69952K->8704K(78656K), 0.0148349 secs] 69952K->22271K(253440K), 0.0148914 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]`

* 2020-10-28T15:10:24.779-0800：GC 事件开始时间，-0800 代表东八区
* GC：用来区分 Minor GC 还是 Full GC，GC 即 Minor GC，Allocation Failure 表示触发 GC 的原因
* DefNew：表示垃圾收集器名称，该名字表示年轻代使用的单线程、标记-复制、STW 垃圾收集器，69952K->8704K 表示垃圾前后的年轻代使用量，(78656K) 表示年轻代总大小
* 69952K->22271K(253440K)：表示垃圾收集前后整个堆的使用量
* 0.0148349 secs：GC 事件持续时间，单位为秒
* [Times: user=0.01 sys=0.01, real=0.02 secs]：此次 GC 事件的持续时间，通过 3 部分衡量：user 部分表示所有 GC 线程消耗的 CPU 时间、syc 部分表示系统调用和系统等待事件消耗的时间、real 表示应用程序暂停的时间。

此次 GC 事件的内存变化如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5exw0jizj30vu0a474p.jpg)

#### Full GC
`2020-10-28T15:10:25.102-0800: [Full GC (Allocation Failure) 2020-10-28T15:10:25.102-0800: [Tenured: 174753K->174761K(174784K), 0.0344597 secs] 253401K->192342K(253440K), [Metaspace: 2725K->2725K(1056768K)], 0.0345244 secs] [Times: user=0.04 sys=0.00, real=0.03 secs]`

* Tenured：该名字表示老年代使用的单线程、标记-清除-整理（mark-sweep-compact）、STW 垃圾收集器
* Metaspace：表示 Metaspace 变化情况

此次 GC 事件的内存变化如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5eydmtqdj30ww0a6wex.jpg)

#### Heap
`Heap
 def new generation   total 78656K, used 77740K [0x00000007b0000000, 0x00000007b5550000, 0x00000007b5550000)
  eden space 69952K, 100% used [0x00000007b0000000, 0x00000007b4450000, 0x00000007b4450000)
  from space 8704K,  89% used [0x00000007b4450000, 0x00000007b4beb098, 0x00000007b4cd0000)
  to   space 8704K,   0% used [0x00000007b4cd0000, 0x00000007b4cd0000, 0x00000007b5550000)
 tenured generation   total 174784K, used 174756K [0x00000007b5550000, 0x00000007c0000000, 0x00000007c0000000)
   the space 174784K,  99% used [0x00000007b5550000, 0x00000007bfff9098, 0x00000007bfff9200, 0x00000007c0000000)
 Metaspace       used 2732K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 296K, capacity 386K, committed 512K, reserved 1048576K`

*  def new generation：年轻代总计 78656K，使用 77740K，方括号中是内存地址信息
	* eden space：占用 69952K，100% used
	* from space：占用 8704K,  89% used
	* to  space：占用 8704K,   0% used
* tenured generation：老年代总计 174784K,，使用 174756K
	* the space：占用 174784K，99% used
* Metaspace：元数据区总计使用 2732K，容量 4486K，JVM 保证可用大小 4864K，保留空间 1056768K
	* class space：使用 296K，容量 386K，JVM 保证可用大小 512K，保留空间 1048576K

### Parallel GC
#### 启动命令
`java -XX:+UseParallelGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

#### Minor GC
`2020-10-28T16:10:36.459-0800: [GC (Allocation Failure) [PSYoungGen: 131510K->21503K(153088K)] 131510K->42586K(502784K), 0.0169498 secs] [Times: user=0.02 sys=0.08, real=0.02 secs]`

* PSYoungGen：年轻代

此次 GC 事件内存变化如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5eysyz5aj30wg0a074p.jpg)

#### Full GC
`2020-10-28T16:10:36.833-0800: [Full GC (Ergonomics) [PSYoungGen: 24296K->0K(116736K)] [ParOldGen: 313471K->238961K(349696K)] 337768K->238961K(466432K), [Metaspace: 2725K->2725K(1056768K)], 0.0331210 secs] [Times: user=0.19 sys=0.01, real=0.03 secs]`

* ParOldGen：老年代

此次 GC 事件内存变化如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5ez1pyhhj30vy0a474o.jpg)

#### Heap
`Heap
 PSYoungGen      total 116736K, used 2921K [0x00000007b5580000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 58880K, 4% used [0x00000007b5580000,0x00000007b585a560,0x00000007b8f00000)
  from space 57856K, 0% used [0x00000007b8f00000,0x00000007b8f00000,0x00000007bc780000)
  to   space 57856K, 0% used [0x00000007bc780000,0x00000007bc780000,0x00000007c0000000)
 ParOldGen       total 349696K, used 325713K [0x00000007a0000000, 0x00000007b5580000, 0x00000007b5580000)
  object space 349696K, 93% used [0x00000007a0000000,0x00000007b3e14698,0x00000007b5580000)
 Metaspace       used 2732K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 296K, capacity 386K, committed 512K, reserved 1048576K`

* PSYoungGen：年轻代
* ParOldGen：老年代

### CMS GC
#### 启动参数
`java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

#### Minor GC
`2020-10-28T16:38:37.222-0800: [GC (Allocation Failure) 2020-10-28T16:38:37.222-0800: [ParNew: 69952K->8703K(78656K), 0.0083268 secs] 69952K->22097K(253440K), 0.0083761 secs] [Times: user=0.01 sys=0.03, real=0.01 secs]`

* ParNew：年轻代

此次 GC 事件后内存变化如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5ezd5iorj30vu0a474p.jpg)

#### Full GC
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5ezjwocoj32jo0ck7am.jpg)

* GC（CMS Initial Mark）：初始标记
* CMS-concurrent-mark：并发标记
* CMS-concurrent-preclean：并发预处理
* GC（CMS Final Remark）：最终标记
* CMS-concurrent-sweep：并发清除
* CMS-concurrent-reset：并发重置

此次 GC 事件内存变化如下图：
![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk5ezta336j30vo098glz.jpg)

#### Heap
`Heap
 par new generation   total 157248K, used 12382K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,   8% used [0x00000007a0000000, 0x00000007a0c17958, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
  to   space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
 concurrent mark-sweep generation total 349568K, used 337264K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 2732K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 296K, capacity 386K, committed 512K, reserved 1048576K`

* par new generation：年轻代
* concurrent mark-sweep generation：老年代

### G1 GC
#### 启动参数
`java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis`

#### Minor GC
> todo

#### Full GC
> todo

#### Heap
> todo
