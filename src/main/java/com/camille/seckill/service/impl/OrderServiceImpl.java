package com.camille.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.camille.seckill.pojo.Order;
import com.camille.seckill.mapper.OrderMapper;
import com.camille.seckill.pojo.SeckillGoods;
import com.camille.seckill.pojo.SeckillOrder;
import com.camille.seckill.pojo.User;
import com.camille.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.camille.seckill.service.ISeckillGoodsService;
import com.camille.seckill.service.ISeckillOrderService;
import com.camille.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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


    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;


    @Override
    @Transactional
    public Order seckill(User user, GoodsVo goods) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsService.updateById(seckillGoods);

        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());
        baseMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);


        return order;
    }
}
