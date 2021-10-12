package com.camille.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id，手机号码
     */
    private Long id;


    private String nickname;

    /**
     * MD5(MD5(pass明文+固定salt) + salt)
     */
    private String password;

    private String slat;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    private LocalDateTime registerDate;

    /**
     * 最后一次登录时间
     */
    private LocalDateTime lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;


}
