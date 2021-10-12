package com.camille.seckill.vo;


import com.camille.seckill.validator.isMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginVo {

    @NotEmpty
    @isMobile
    private String mobile;

    @NotEmpty
    @Length(min = 32)
    private String password;
}
