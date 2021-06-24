package com.turbo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turbo.seckill.pojo.Goods;
import com.turbo.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 跳转商品详情页
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
