package cn.lishe.javabase.validator;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author YeJin
 * @date 2019/11/27 16:45
 */
public class PhoneNoValidator implements ConstraintValidator<PhoneNo, String> {
    @Override
    public void initialize(PhoneNo constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isBlank(phoneNo)) {
            return false;
        }

        return phoneNo.matches("^1[0-9]{10}$");
    }
}
