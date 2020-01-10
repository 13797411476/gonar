package cn.lishe.javabase.validator;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author YeJin
 * @date 2019/11/27 16:45
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {
    @Override
    public void initialize(Username constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isBlank(username)) {
            return false;
        }

        return username.matches("^[a-zA-Z][a-zA-Z0-9_\\-]{4,15}$");
    }
}
