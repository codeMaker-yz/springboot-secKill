package com.camille.seckill.vo;

import com.camille.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {


    private BigDecimal seckillPrice;


    private Integer stockCount;


    private LocalDateTime startDate;


    private LocalDateTime endDate;

}
