package com.turbo.seckill.exception;

import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @Author Jusven
 * @Date 2021/6/17 21:36
 *
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
//            return ResponseBean.error(ex.getResp);
        }else if(e instanceof BindException){
            BindException ex = (BindException) e;
            ResponseBean responseBean = ResponseBean.error(ResponseBeanEnum.BIND_ERROR);
            responseBean.setMessage("参数校验异常：" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return responseBean;
        }
        return ResponseBean.error(ResponseBeanEnum.ERROR);
    }
}
