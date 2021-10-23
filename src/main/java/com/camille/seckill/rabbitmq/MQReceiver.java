package com.camille.seckill.rabbitmq;


import com.camille.seckill.pojo.SeckillMessage;
import com.camille.seckill.pojo.SeckillOrder;
import com.camille.seckill.pojo.User;
import com.camille.seckill.service.IGoodsService;
import com.camille.seckill.service.IOrderService;
import com.camille.seckill.utils.JsonUtil;
import com.camille.seckill.vo.GoodsVo;
import com.camille.seckill.vo.RespBean;
import com.camille.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接受的消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();
        GoodsVo goods = goodsService.findGoodsById(goodId);
        if(goods.getStockCount() < 1){
            return;
        }
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodId);
        if(seckillOrder != null){
            return;
        }
        orderService.seckill(user,goods);



    }

}
