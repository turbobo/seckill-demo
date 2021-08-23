# seckill-demo
商品秒杀-练习
技术点


学习目标





项目搭建






删掉文件


pom添加注释--->yml配置--->建目录service,pojo,controller---->配置扫描包：
@SpringBootApplication
@MapperScan("com.turbo.seckill.pojo")
--->写测试controller,加注解：
@Controller
@RequestMapping("/demo")

建表：
password VARCHAR ( 32 ) DEFAULT NULL COMMENT 'MD5(MD5(pass铭文+固定salt)+salt)',
密码在客户端加密一次，服务端再加密一次
引入md5依赖--->创建MD5Util 工具类，加上@Component注解


mybatis-plus逆向工程
1. 创建spring web项目
2. 引入依赖
<!--mybatis-plus依赖-->
<dependency>
<groupId>com.baomidou</groupId>
<artifactId>mybatis-plus-boot-starter</artifactId>
<version>3.4.3</version>
</dependency>
<!--代码生成器 依赖-->
<dependency>
<groupId>com.baomidou</groupId>
<artifactId>mybatis-plus-generator</artifactId>
<version>3.4.1</version>
</dependency>
<!--Freemarker模板引擎-->
<dependency>
<groupId>org.freemarker</groupId>
<artifactId>freemarker</artifactId>
<version>2.3.31</version>
</dependency>
<!--mysql-->
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<scope>runtime</scope>
</dependency>

3. CodeGenerator代码生成器配置--->模板拷贝到templates目录下--->生成的包pojo,service,controller, mapper.xml拷贝到项目中即可
实体类的注解 @Data   //提供类的get、set、equals、hashCode、canEqual、toString方法

4.@RestController使用此注解的方法表示一个控制器，返回json。
原来返回一个json需要@Controller和@RequestBody配合使用。
使用@Controller会返回一个html 和jsp页面。

5.mapper.xml文件放在resources目录下，通过applicaiton.yml配置
mapper-locations: classpath*:/mapper/*Mapper.xml
Free Mybatis Plugin插件
mapper接口直接跳转到mapper.xml，自动生成语句。

引入验证validation
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-validation</artifactId>
</dependency>

public ResponseBean doLogin(@Valid LoginVo loginVo){}
@NotNull
private String mobile;

@NotNull
@Length(min = 32)
private String password;

分布式session



linux安装redis，参考centos7安装文档

springsession实现分布式session
1. 引入依赖
<!-- spring data redis 依赖 -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- commons-pool2对象池依赖 -->
<dependency>
<groupId>org.apache.commons</groupId>
<artifactId>commons-pool2</artifactId>
</dependency>
<!-- spring session依赖 -->
<dependency>
<groupId>org.springframework.session</groupId>
<artifactId>spring-session-data-redis</artifactId>
</dependency>

2. application.yml配置
redis:
host: 192.168.88.129
port: 6379
  #数据库
  database: 0
  #超时时间
  timeout: 10000ms
lettuce:
pool:
#最大连接数，默认8
      max-active: 8
      #最大连接阻塞等待时间，默认-1
      max-wait: 10000ms
#最大空闲连接，默认8
      max-idle: 200
      #最小空闲连接，默认0
      min-idle: 5

redis实现session分布式存储
1. 去除pom文件 spring session依赖
2. 自定义 redis 配置类
3. 用户登录信息不放在session中了，放在redis里
//用户信息存入session
//        request.getSession().setAttribute(ticket,user);

        //用户信息存入redis
        redisTemplate.opsForValue().set("user:" + ticket,user);

//使用cookie记录uuid,以便取出session或者 redis
        CookieUtil.setCookie(request,response,"userTicket",ticket);
4. 用户信息从redis中获取，修改 UserServiceImpl、GoodsController
5. 每个业务逻辑都需要判断用户是否登录，把该判断抽逻辑取出来：
自定义用户参数UserArgumentResolver，登录用户注解LoginUser，自定义MVC配置类WebConfig
项目启动记得启动linux、redis
启动redis：
cd /usr/local/redis/bin
 ./redis-server redis.conf
自定义webmvc配置
自定义 之 使用参数注解获取当前用户（基于参数解析器HandlerMethodArgumentResolver）

● 首先定义一个参数类型的注解  @LoginUser
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}


● 自定义参数解析器UserArgumentResolver：
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver

然后实现下面两个方法：
1. public boolean supportsParameter(MethodParameter parameter) 返回true才会执行第二个方法
2. public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception


● 自定义mvc配置类WebConfig：
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer

1. 将自定义参数解析器注入：
@Override
public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//添加到 自定义用户参数解析器
    resolvers.add(userArgumentResolver);
}
2. 自定义资源处理器，否则静态资源无法访问
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
}


● 获取当前登录用户：前面加上 @LoginUser注解即可
@RequestMapping("/toList")
public String toList(Model model, @LoginUser User user){

@RequestMapping("/doSeckill")
public String doSeckill(Model model, @LoginUser User user, Long goodsId){
逻辑实现
1. controller首先需要查数据，就要注入mapper
2. 写sql语句，先去数据执行，再写入mapper
3.   页面标签加name属性，传到后台就会带上参数名
<input type="hidden" id="goodsId" name="goodsId" th:value="${goods.id}" />
public String doSeckill(Model model, User user, Long goodsId)
项目启动
先启动linux，
再启动redis：cd /usr/local/redis/bin    ./redis-server redis.conf
启动redis客户端
jmeter
● windows版本：E:\Develop\apache-jmeter-5.3\bin目录下，运行jmeter.bat

默认设置：jmeter.properties
#language=en
language=zh_CN   默认中文

sampleresult.default.encoding=UTF-8  防止中文乱码

使用：
1. 新建http默认值


2.  新建http请求

3.  查看结果




● linux版本
解压到  /usr/local/ 目录下：
tar zxvf apache-jmeter-5.4.1.tgz  -C  /usr/local/
修改配置：编码格式

将windows测试过的 jmeter脚本 first.jmx上传到 jmeter/bin目录;
运行：./jmeter.sh -n -t first.jmx -l result.jtl
-n 非图迅界面运行
-t 测试脚本文件 first.jmx
-l 记录结果到  .jtl文件中
再将linux运行的result.jtl文件放到windows的jmeter中查看报告

QPS每秒查询率(Query Per Second) ：服务器每秒能够响应的查询 / 请求次数，是一台服务器每秒能够相应的查询次数，是对一个特定的查询服务器在规定时间内所处理流量多少的衡量标准, 即每秒的响应请求数，也即是最大吞吐能力。
TPS事务数/秒(Transaction Per Second)：一个事务是指一个客户机向服务器发送请求然后服务器做出反应的过程。客户机在发送请求时开始计时，收到服务器响应后结束计时，以此来计算使用的时间和完成的事务个数。

linux部署
linux安装mysql ====》新建linux数据库seckill=====》项目修改为 linux Mysql账号密码
注：建议本地 mysql密码和 linux  mysql密码保持一致

/root/projects目录下
部署打包：idea Maven springboot 打包跳过test代码 ===》clean===》package===>jar包上传到linux
运行：java -jar 
访问：http://192.168.88.129:8080/login/toLogin

用户压力测试
1. 同一个用户压力测试
先登录一个用户，拿到cookie值，然后填入jmeter参数中


2.  多用户压测
先登录两个用户，分别记录cookie，写入文档中；---编码格式utf-8===》然后在jmeter中添加csv配置===》添加httpCookie配置




压测结果：
秒杀商品库存出现错误，商品数量已经为负数，但是仍热秒杀成功，也生成了部分订单


优化
页面缓存
把页面缓存到redis：把静态的页面内容做缓存
1. 方法不再返回页面，返回数据，加上@ResponseBody；
设置页面返回数据类型
@RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")

注解RequestMapping中produces属性可以设置返回数据的类型以及编码，可以是json或者xml
2. 引入redis  
@Autowired
private RedisTemplate redisTemplatel;

* windows 优化前：压测    30000次  qps：385.1    258.6
* windows 页面缓存后压测  30000次  qps：385.1    841.5

url缓存
@RequestMapping("/toDetail/{goodsId}")
public String toDetail(Model model, User user, @PathVariable Long goodsId){
把商品id不同的页面做缓存
对象缓存

注：数据库更新后，要更新redis中的相关数据，例如：用户更新了密码，但是redis缓存中还是之前的用户信息


toList请求获取redis中缓存的列表页面goodsList.html，

商品详情页静态化
goodsDetail.htm页面的初始化方法去加载数据



解决库存超卖
1. 减库存之前判断库存大于0
2. 同一用户发起多个请求：两个用户都进入，此时判断都没有产生订单，那么两个用户同时写入就会有问题。



添加唯一索引
使用联合唯一索引确保用户不会秒杀同一件商品多次


把秒杀订单信息放到redis中


订单数据还有问题


判断更新语句是否成功
减库存时，当前库存必须大于0，更新语句执行成功，才去创建一个订单

boolean seckillGoodsFlag = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql(
        "stock_count = stock_count - 1").eq("goods_id", goods.getId()).gt("stock_count", 0));
if(!seckillGoodsFlag){
    return null;
}


项目引入rabbitmq
启动mq
systemctl start rabbitmq-server.service
查看状态
systemctl status rabbitmq-server.service

访问
http://192.168.88.129:15672/    虚拟机ip
开启远程登录  guest/guest
cd /etc/rabbitmq/
vim rabbit.config
[{rabbit,[{loopback_users,[]}]}].


1. 添加依赖
<!-- amqp 依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

2.  application.yml 配置rabbitmq  
3.  准备消息生成者、消费者





Direct模式：

Topic模式

reids预减库存
1. 项目初始化时，把商品库存加载到redis中
2. 通过redis进行预减库存，判断是否为空（添加内存标记，减少redis访问）
3. 预减库存成功才生成秒杀订单


项目启动一直在发送消息，需要修改 hikari数据库拦截池配置



redis分布式锁
1. 设置锁  
2. 获取锁
3. 比较锁
4. 删除锁

使用Lua脚本的好处如下:
● 1.减少网络开销：本来5次网络请求的操作，可以用一个请求完成，原先5次请求的逻辑放在redis服务器上完成。使用脚本，减少了网络往返时延。
● 2.原子操作：Redis会将整个脚本作为一个整体执行，中间不会被其他命令插入。
● 3.复用：客户端发送的脚本会永久存储在Redis中，意味着其他客户端可以复用这一脚本而不需要使用代码完成同样的逻辑。
服务优化总结
目的：减少数据库访问
1. 系统初始化时，把商品库存加载到redis
2. 收到秒杀请求时，redis进行预减库存
3. 请求进入队列
4. 队列监听，消息被消费，去生成订单
5. 客户端轮询是否下单成功

安全优化
● 防止秒杀接口暴露，被恶意脚本请求 ---》 隐藏接口，先通过接口去获取真正的秒杀接口
 	减少前期并发，第一次获取到的是接口地址，还需要二次请求去请求数据库；

点击秒杀后，首先去创建一个秒杀接口路径，也就是一个用户+商品id+uuid组成的字符串，存入redis，设置60秒有效；
第二次请求会拿着上述path字符串去执行操作，此时后台会判断这个path是否和redis中相同。




● 接口防刷：使用复杂验证码：数学公示
借助网上的验证码插件；

秒杀开始后，把验证码存入redis，设置300s有效；
每次刷新验证码覆盖原来的验证码；

请求接口实际地址的时候，同时把页面输入的验证码传过去，后台验证码是否正确。


● 接口限流
限流算法： https://www.cnblogs.com/linjiqin/p/9707713.html

计数器法
计数器算法是限流算法里最简单也是最容易实现的一种算法。比如我们规定，对于A接口来说，我们1分钟的访问次数不能超过100个。那么我们可以这么做：在一开 始的时候，我们可以设置一个计数器counter，每当一个请求过来的时候，counter就加1，如果counter的值大于100并且该请求与第一个 请求的间隔时间还在1分钟之内，那么说明请求数过多；如果该请求与第一个请求的间隔时间大于1分钟，且counter的值还在限流范围内，那么就重置 counter

每个接口都要进行限流，所以创建自定义注解拦截器进行限流
自定义拦截器AccessLimitInterceptor
拦截器从用户线程中获取用户，接着进行限流

获取用户线程使用ThreadLocal，保证线程绑定到用户上，当前登录用户存在自己的线程中，用户值只有当前线程才能看得到


总结
秒杀订单放入redis：
1. 处理订单速度快；
2. 方便用户下单后去查询订单；



  
  
