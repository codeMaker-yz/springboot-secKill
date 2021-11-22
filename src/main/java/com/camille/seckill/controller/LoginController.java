package com.camille.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.camille.seckill.pojo.User;
import com.camille.seckill.service.IUserService;
import com.camille.seckill.vo.LoginVo;
import com.camille.seckill.vo.RespBean;
import com.camille.seckill.vo.RespBeanEnum;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController implements InitializingBean {
    @Autowired
    private IUserService userService;

    private static final int USER_COUNT = 100000;

    private BloomFilter<Long> userBloom = BloomFilter.create(Funnels.longFunnel(),USER_COUNT);

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Validated LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        if(userBloom.mightContain(Long.parseLong(loginVo.getMobile()))) {
            System.out.println("用户可能存在");
            return userService.doLogin(loginVo, request, response);
        }
        System.out.println("用户不存在");
        return RespBean.error(RespBeanEnum.LOGIN_ERROR);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<User> users = userService.list(null);
        users.forEach(user -> userBloom.put(user.getId()));
    }
}
