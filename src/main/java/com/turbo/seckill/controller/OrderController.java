package com.turbo.seckill.controller;


import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IOrderService;
import com.turbo.seckill.vo.OrderDetailVo;
import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @RequestMapping("detail")
    @ResponseBody
    public ResponseBean detail(User user, Long orderId) {
        if (user == null) {
            ResponseBean.error(ResponseBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detailVo = orderService.detail(orderId);
        return ResponseBean.success(detailVo);
    }
}
