package com.camille.seckill.service;

import com.camille.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.camille.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-12
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
