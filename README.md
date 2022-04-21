# springboot-secKill

## 环境

- SpringBoot 2.5.5
- mysql 8.0.11
- redis 5.0.5
- rabiitMQ 3.9.7

### 使用mybatis-plus逆向生成代码流程

 1. 引入mybatis-plus相关依赖
      ```java
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.1</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.3.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.0</version>
        </dependency>
 
 
 2. 编写CodeGenertor工具类(存放在utils中)
 
 3. 执行CodeGenertor工具类，输入数据库的表名，完成代码生成


### 自定义注解实现流程

引入依赖
    
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
实现登录模块中，需要对用户的账号进行是否为手机号的判断，因此自定义了一个`@isMobile`注解，实现流程如下：

#### 1.自定义注解类
 ```java
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface isMobile {

    boolean required() default true;

    String message() default "手机号码格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
 
 ```
 
 **@Constraint：指定校验类
   @Target：该注解可以声明在哪些目标元素之前，也可理解为注释类型的程序元素的种类
   @Retention：注解类的生命周期
 **

#### 2.自定义手机号码验证规则
  ```java
   public class IsMobileValidator implements ConstraintValidator<isMobile,String> {
    
    private boolean required = false;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(s);
        } else {
            if(!StringUtils.hasText(s)){
                return true;
            } else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }

    @Override
    public void initialize(isMobile constraintAnnotation) {
        required = constraintAnnotation.required();

    }
 }
 ```

**校验类实现ConstraintValidator接口，接口使用了泛型，需要指定两个参数，第一个参数是自定义的注解类，第二个参数是需要校验的数据类型。**

`initalize`和`isValid`方法需要override。在initalize中进行初始化操作，isValid中是校验方法，如果校验不通过，会产生BindException异常，捕获异常后可以获取到自定义注解中定义的内容。

#### 3.在需要校验的参数前添加@Validated注解


### LocalDateTime类

```java
// 获取当前时间
LocalDateTime now = LocalDateTime.now();
LocalDateTime end = LocalDateTime.now();

// 计算两个时间的差
Duration duration = Duration.between(now, end);

// 相差的天数
long days = duration.toDays();

// 相差的小时数
long hours = duration.toHours();

// 相差的分钟数
long minutes = duration.toMinutes();

// 相差的毫秒数
long millis = duration.toMillis();

// 相差的纳秒数
long nanos = duration.toNanos();
```

## 优化手段
   
   优化的核心是：减少对数据库的访问，减少网络流量的消耗。

   1.页面缓存：通过redis数据库缓存读多写少的页面
   
   2.对象缓存：
   
   3.页面静态化(前后端分离后，除了第一次访问外，后续访问访问本地的html页面（前端页面缓存在浏览器中），与后端的数据交互通过ajax来完成，前后端不分离时，后端需要返回将数据渲染到html页面后，将整个html页面返回，耗费网络流量。)
   
   4.使用redis预减库存：系统初始化时，将秒杀商品库存数量加载到redis中，请求过来后，判断是否还有库存，如果库存已经为0，直接返回，不需要访问Mysql数据库。
   
   5.使用内存标记：当库存没有时，请求虽然不需要查询数据库，但会大量请求到redis上，可以通过一个内存标记，当库存为0时，直接返回，不需要访问redis数据库。
   
   
   

## 项目部署到阿里云服务器流程

 1.配置阿里云服务器环境，安装jdk8和docker；

 2.docker中安装mysql 8.0.11 和 redis 5.0.5镜像；

安装mysql后，新建一个数据库seckill用于本项目，新建一个用户授予操作该数据库的权限

```mysql
CREATE USER 'xxxx'@'%' IDENTIFIED BY 'password';  # '%'设置允许外网连接（Navicat），新建一个用户；

grant all privileges on `seckill`.* to 'xxxx'@'%'; # 授予用户xxxx操作seckill数据库的所有权限；

```

 3.maven项目的Lifecycle中先点击clean，再点击package进行打包

 4.在target文件夹中找到对应的jar包，通过Xftp上传到云服务器中

 5.通过命令 java -jar xxxxxxxxxx.jar 包运行程序




## Cookie问题：本地运行时，浏览器可以存放cookie，放在阿里云上时，浏览器没有存放cookie。

### 通过在浏览器F12查看cookie时发现，访问阿里云服务器时，cookie没有写入，解决步骤：

  **1. 查看浏览器是否禁用cookie，本地运行时cookie可用，因此不是该原因。**
  
  **2. 查看CookieUtil工具类发现，在setCookie时需要用到获取domain，发现是因为代码提取的domain与ip地址不同，导致浏览器没有生成cookie，修改getDomainName方法后，该问题成功解决。**
  
  **原因猜测是浏览器对不合法的domain设置的cookie不生效**




## 超卖问题（订单数量 > 库存数量）

在执行秒杀的过程中，需要更新数据库库存，生成订单、生成秒杀订单三个操作，需要以事务来执行，seckill方法需要加上@Transactional注解。

减库存方法：
```java
seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1").eq("id", seckillGoods.getId())
                .gt("stock_count", 0));
```

防止同一用户重复购买：数据库用userId和goodsId作为唯一索引，可以防止同一用户相同商品的重复插入数据库。
唯一索引和普通索引在查询能力上没有太大区别，但对更新性能会有影响。
对于唯一索引，所有的更新操作都要先判断这个操作是否违反唯一性约束。比如，要插入（userId,goodsId）这条记录，就要先判断表中是否存在这条记录，这必须要将数据页读入内存才能判断。
而对于普通索引，要插入数据时，如果目标页不在内存中，普通索引会将更新记录在change buffer中，而唯一索引无法利用change buffer，必须将目标页读入内存。
change buffer减少了随机磁盘访问，对更新性能提升明显，但也要针对具体的使用场景。
如果是写多读少的业务，页面在写完后马上被访问到的概率比较小，此时change buffer 使用效果最好。常见的业务模型：账单类、日志类系统。
但如果一个业务的更新模式是写入后马上回查询，就会立马触发merge过程。这样随机访问IO的次数不会减少，反而增加了change buffer的维护代价。秒杀场景？


创建成功的订单，以userId+goodsId作为key存储在redis中，防止重复购买（避免判断同一用户时查询mysql数据库）


## docker部署rabbitMQ

```linux

docker pull rabbitmq

docker run -d --hostname my-rabbit --name rabbit -e RABBITMQ_DEFAULT_USER=你的user -e RABBITMQ_DEFAULT_PASS=你的password -p 15672:15672 -p 5672:5672 rabbitmq:management
```

## SpringBoot集成rabbitMQ

1. 引入依赖
```java
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

2. 配置yml文件

## 布隆过滤器

引入依赖
```java
<dependency>
  <groupId>com.google.guava</groupId>
  <artifactId>guava</artifactId>
  <version>29.0-jre</version>
</dependency>
```

服务启动时，将数据库中的用户id预先加载进内存中，对进行登录的请求，进行id验证，如果布隆过滤器中不存在，则直接返回，不对数据库进行查询。

布隆过滤器是基于位图的一种改进。

假设有1千万的32位整形数据，数据范围是1到1亿之间，如果使用散列表存储，至少需要40MB存储空间，但通过位图的话，只用1亿个2进制位，大约是12MB的存储空间就可以了。

但位图要求数据范围是1亿，如果数据范围是10亿大小，就需要120MB的存储空间了。

针对这一问题，就产生了布隆过滤器。如果数据范围是10亿，布隆过滤器的做法是仍然使用1亿个二进制大小的位图，然后通过hash函数，对数字或者字符进行处理，让他落到1-1亿范围内。

如果只使用一个hash函数，会产生大量的冲突，因此该算法采用K个hash函数，得到K个不同hash值：X1,X2,...,XK。将这K个数字作为位图中的下标，BitMap[X1],BitMap[X2],...,BitMap[XK]
都设置为true。就是用K个二进制位，来表示一个数字的存在。

如果一个值可能存在，则这K个二进制位都为1，如果有一个二进制位为0，则该值一定不存在。

为什么是可能存在呢？因为布隆过滤器存在误判问题，但只会对存在的情况有误判；如果某个数字不存在，说明这个数字真的不存在。


