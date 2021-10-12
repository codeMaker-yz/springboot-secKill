package com.camille.seckill.service.impl;

import com.camille.seckill.pojo.Goods;
import com.camille.seckill.mapper.GoodsMapper;
import com.camille.seckill.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.camille.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mrZhang
 * @since 2021-10-12
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {



    @Override
    public List<GoodsVo> findGoodsVo() {

        return baseMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsById(long GoodsId) {
        GoodsVo goodsVo = baseMapper.findGoodsById(GoodsId);
        return goodsVo;
    }


}
