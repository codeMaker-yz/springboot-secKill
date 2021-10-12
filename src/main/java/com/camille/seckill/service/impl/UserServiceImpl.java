package com.camille.seckill.service.impl;

import com.camille.seckill.exception.GlobalException;
import com.camille.seckill.pojo.User;
import com.camille.seckill.mapper.UserMapper;
import com.camille.seckill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.camille.seckill.utils.*;
import com.camille.seckill.vo.LoginVo;
import com.camille.seckill.vo.RespBean;
import com.camille.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User getUserByTicket(String ticket, HttpServletRequest request, HttpServletResponse response) {
        if(!StringUtils.hasText(ticket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + ticket);
        if(null != user){
            CookieUtil.setCookie(request, response, "userTicket", ticket);
        }
        return user;
    }

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        /*if(!StringUtils.hasText(mobile)||!StringUtils.hasText(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }*/

        User user = baseMapper.selectById(mobile);
        if(user == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        if(!MD5Util.fromPassToDBPass(password, user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        //request.getSession().setAttribute(ticket, user);
        //CookieUtil.setCookie(request, response, "userTicket", ticket);
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }
}
