# springboot-secKill

## 环境

- SpringBoot 2.5.5
- mysql 8.0
- redis 5.0.5

### 使用mybatis-plus逆向生成代码

#### 使用流程：

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

  
