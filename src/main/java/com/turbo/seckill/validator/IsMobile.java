package com.turbo.seckill.validator;

import com.turbo.seckill.vo.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * @Author Jusven
 * @Date 2021/6/13 13:53
 * 验证手机号
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(NotNull.List.class)
@Documented
@Constraint(   //自定义校验规则
        validatedBy = {IsMobileValidator.class}
)
public @interface IsMobile {
    //必填属性
    boolean required() default true;

    String message() default "手机号码格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
