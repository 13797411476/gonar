package cn.lishe.javabase.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author YeJin
 * @date 2019/11/27 16:45
 */
public class InValidator implements ConstraintValidator<In, String> {

    private String[] in;

    @Override
    public void initialize(In e) {
        in = e.values();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        for (String s : in) {
            if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
