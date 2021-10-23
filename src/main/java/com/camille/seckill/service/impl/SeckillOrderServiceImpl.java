package com.camille.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.camille.seckill.pojo.SeckillOrder;
import com.camille.seckill.mapper.SeckillOrderMapper;
import com.camille.seckill.pojo.User;
import com.camille.seckill.service.ISeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = baseMapper.selectOne(new QueryWrapper<SeckillOrder>().eq(
                "user_id", user.getId()
        ).eq("goods_id", goodsId));
        if(seckillOrder != null){
            return seckillOrder.getOrderId();
        } else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        } else {
            return 0L;
        }

    }
}
