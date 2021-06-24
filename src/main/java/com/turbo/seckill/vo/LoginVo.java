package com.turbo.seckill.vo;

import com.turbo.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author Jusven
 * @Date 2021/6/12 21:35
 * 接收登录参数
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)  //最小长度 32
    private String password;
}
