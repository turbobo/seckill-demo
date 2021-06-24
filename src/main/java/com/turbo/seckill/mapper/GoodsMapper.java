package com.turbo.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turbo.seckill.pojo.Goods;
import com.turbo.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
