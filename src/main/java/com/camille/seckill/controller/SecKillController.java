package com.camille.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.camille.seckill.pojo.Order;
import com.camille.seckill.pojo.SeckillMessage;
import com.camille.seckill.pojo.SeckillOrder;
import com.camille.seckill.pojo.User;
import com.camille.seckill.rabbitmq.MQSender;
import com.camille.seckill.service.IGoodsService;
import com.camille.seckill.service.IOrderService;
import com.camille.seckill.service.ISeckillOrderService;
import com.camille.seckill.service.impl.GoodsServiceImpl;
import com.camille.seckill.utils.JsonUtil;
import com.camille.seckill.vo.GoodsVo;
import com.camille.seckill.vo.RespBean;
import com.camille.seckill.vo.RespBeanEnum;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();


    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill (User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //内存标记
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        Long stock = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
        if(stock < 0){
            EmptyStockMap.put(goodsId,true);
            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendKillMessage(JsonUtil.object2JsonStr(seckillMessage));

        return RespBean.success(0);

    }

    /*
    获取秒杀结果
     */
    @GetMapping(value = "/result")
    @ResponseBody
    public RespBean getResult(User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
            EmptyStockMap.put(goodsVo.getId(),false);
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(), goodsVo.getStockCount());
        });

    }
}
