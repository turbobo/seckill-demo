package com.turbo.seckill.exception;

import com.turbo.seckill.vo.ResponseBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Jusven
 * @Date 2021/6/17 21:36
 *
 * 全局异常
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private ResponseBeanEnum responseBeanEnum;
}
