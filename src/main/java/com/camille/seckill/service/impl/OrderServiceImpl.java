package com.camille.seckill.service.impl;

import com.camille.seckill.pojo.Order;
import com.camille.seckill.mapper.OrderMapper;
import com.camille.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-12
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
