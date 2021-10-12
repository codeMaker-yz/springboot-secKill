package com.camille.seckill.service;

import com.camille.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.camille.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-12
 */
public interface IGoodsService extends IService<Goods> {

    //获取商品列表
    List<GoodsVo> findGoodsVo();


    GoodsVo findGoodsById(long GoodsId);
}
