package com.turbo.seckill.vo;

import com.turbo.seckill.utils.ValidatorUtil;
import com.turbo.seckill.validator.IsMobile;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validator;

/**
 * @Author Jusven
 * @Date 2021/6/17 21:17
 * 手机号验证-规则
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean require = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(require){  //必填
            return ValidatorUtil.isMobile(value);
        }else{
            if(StringUtils.isEmpty(value)){
                return false;
            }else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
