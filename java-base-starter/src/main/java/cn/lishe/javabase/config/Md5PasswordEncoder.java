package cn.lishe.javabase.config;

import cn.lishe.javabase.util.Md5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author YeJin
 * @date 2019/11/7 10:46
 */
@Component
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Md5Util.md5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence raw, String mi) {
        return encode(raw).equals(mi);
    }
}
