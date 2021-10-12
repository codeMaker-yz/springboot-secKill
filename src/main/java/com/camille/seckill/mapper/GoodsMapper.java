package com.camille.seckill.mapper;

import com.camille.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.camille.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-12
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();


    GoodsVo findGoodsById(long GoodsId);
}
