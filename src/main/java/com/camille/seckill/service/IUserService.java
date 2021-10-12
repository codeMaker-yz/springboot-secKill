package com.camille.seckill.service;

import com.camille.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.camille.seckill.vo.LoginVo;
import com.camille.seckill.vo.RespBean;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-10
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByTicket(String ticket, HttpServletRequest request, HttpServletResponse response);
}
