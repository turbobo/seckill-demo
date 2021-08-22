package com.turbo.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turbo.seckill.config.LoginUser;
import com.turbo.seckill.exception.GlobalException;
import com.turbo.seckill.pojo.Order;
import com.turbo.seckill.pojo.SeckillMessage;
import com.turbo.seckill.pojo.SeckillOrder;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.rabbitmq.MQSender;
import com.turbo.seckill.service.IGoodsService;
import com.turbo.seckill.service.IOrderService;
import com.turbo.seckill.service.ISeckillOrderService;
import com.turbo.seckill.utils.JsonUtils;
import com.turbo.seckill.vo.GoodsVo;
import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
/**
 * @Author Jusven
 * @Date 2021/6/22 17:14
 * 秒杀
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> redisScript;

    //通过商品id表示库存是否为空
    private Map<Long, Boolean> EmptyStockMap = new HashMap<Long, Boolean>();

    /**
     * 秒杀接口
     * 优化前：windows压测  30000次  qps：154.0   t_order订单数据不正常，t_seckill_goods秒杀商品库存也不正常
     * 优化前：linux压测  30000次  qps：114.4   t_order订单数据不正常，t_seckill_goods秒杀商品库存也不正常
     * <p>
     * 优化后：windows压测  30000次  qps：534.3S   t_order订单数据不正常，t_seckill_goods秒杀商品库存也不正常
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, @LoginUser User user, Long goodsId) {
        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errMsg", ResponseBeanEnum.EMPTY_STOCK.getMessage());
            return "seckillFail";
        }
        //判断用户是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
                eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errMsg", ResponseBeanEnum.REPEAT_ERROR.getMessage());
            return "seckillFail";
        }
        Order order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "orderDetail";
    }

    /**
     * 秒杀接口
     * * 优化前：windows压测  30000次  qps：154.0   t_order订单数据不正常，t_seckill_goods秒杀商品库存也不正常
     * * 优化前：linux压测  30000次  qps：114.4   t_order订单数据不正常，t_seckill_goods秒杀商品库存也不正常
     * *
     * * 缓存后：windows压测  30000次  缓存qps：534.3   t_order订单数据不正常，t_seckill_goods秒杀商品库存也不正常
     * * 缓存后：linux压测   30000次  缓存qps：329.9   t_order订单数据正常，t_seckill_goods秒杀商品库存也不正常
     * <p>
     * 优化后  windows     30000次  缓存qps  330.2   数据库订单数据正常，redis数据正正常
     * lua脚本优化后   windows     10000次  缓存qps  185.6   数据库订单数据正常，redis数据正正常
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean doSeckill(@PathVariable String path, @LoginUser User user, Long goodsId) throws Exception {
        if (null == user) {
            return ResponseBean.error(ResponseBeanEnum.SESSION_ERROR);
        }
/*//        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 1){
            model.addAttribute("errMsg", ResponseBeanEnum.EMPTY_STOCK.getMessage());
            return ResponseBean.error(ResponseBeanEnum.EMPTY_STOCK);
        }
        //判断用户是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
//                eq("user_id",user.getId()).eq("goods_id",goodsId));

        //从redis中获取订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order" + user.getId() + ":" + goods.getId());
        if(seckillOrder != null){
//            model.addAttribute("errMsg",ResponseBeanEnum.REPEAT_ERROR.getMessage());
            return ResponseBean.error(ResponseBeanEnum.REPEAT_ERROR);
        }
        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return ResponseBean.success(order);*/

        ValueOperations valueOperations = redisTemplate.opsForValue();

        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return ResponseBean.error(ResponseBeanEnum.REQUEST_ILLEGAL);
        }

        //判断用户是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return ResponseBean.error(ResponseBeanEnum.REPEAT_ERROR);
        }

        //如果库存已经为0，但是还会收到请求，加大了开销
        //使用内存标记,减少redis访问
        if (EmptyStockMap.get(goodsId)) {
            return ResponseBean.error(ResponseBeanEnum.EMPTY_STOCK);
        }

        //预减库存 （原子操作）
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId),
                Collections.EMPTY_LIST);
        if (stock < 0) {
            //设置redis不可访问
            EmptyStockMap.put(goodsId, true);
            //重置库存为 0
            valueOperations.increment("seckillGoods:" + goodsId);
            //返回空库存异常
            return ResponseBean.error(ResponseBeanEnum.EMPTY_STOCK);
        }
        //生成秒杀订单
//        Order order = orderService.seckill(user, goods);
        //消息对象
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtils.objectToJson(seckillMessage));
        // 0表示排队中
        return ResponseBean.success(0);
    }

    /**
     * 系统初始化，把商品数量库存加载到 redis
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
                    redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
                    EmptyStockMap.put(goodsVo.getId(), false);
                }
        );
    }

    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return orderId：成功  -1：失败  0：排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBean getResult(@LoginUser User user, String goodsId) {
        if (user == null) {
            return ResponseBean.error(ResponseBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return ResponseBean.success(orderId);

    }

    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBean getPath(@LoginUser User user, Long goodsId, String captcha) {
        if (user == null) {
            return ResponseBean.error(ResponseBeanEnum.SESSION_ERROR);
        }
        //校验验证码
        boolean check = orderService.checkCpatcha(user, goodsId, captcha);
        if (!check) {
            return ResponseBean.error(ResponseBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user, goodsId);
        return ResponseBean.success(str);
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void captcha(@LoginUser User user, Long goodsId, HttpServletResponse response) {
        if (null == user || goodsId < 0) {
            throw new GlobalException(ResponseBeanEnum.REQUEST_ILLEGAL);
        }
        // 设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

/*        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);*/

        // 生成验证码，存入redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        //300秒过期, 每次刷新后直接覆盖
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);

        // 输出图片流
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败" + e.getMessage());
        }
    }
}
