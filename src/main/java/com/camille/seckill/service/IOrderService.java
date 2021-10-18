package com.camille.seckill.service;

import com.camille.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.camille.seckill.pojo.User;
import com.camille.seckill.vo.GoodsVo;
import com.camille.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-12
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}
