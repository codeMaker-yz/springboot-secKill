package com.camille.seckill.vo;

import com.camille.seckill.utils.ValidatorUtil;
import com.camille.seckill.validator.isMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<isMobile,String> {

    private boolean required = false;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(s);
        } else {
            if(!StringUtils.hasText(s)){
                return true;
            } else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }

    @Override
    public void initialize(isMobile constraintAnnotation) {
        required = constraintAnnotation.required();

    }
}
