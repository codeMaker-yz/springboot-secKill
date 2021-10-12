# springboot-secKill

## 环境

- SpringBoot 2.5.5
- mysql 8.0
- redis 5.0.5

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

 1.自定义手机号码验证规则
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

的






