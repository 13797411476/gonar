package cn.lishe.javabase.util;

import cn.lishe.javabase.define.LsConstants;
import cn.lishe.javabase.define.UserSession;
import cn.lishe.javabase.properites.AuthProperties;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author YeJin
 * @date 2019/11/7 11:13
 */
@Component
public class AuthRedisUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AuthProperties authProperties;

    public void setData2Redis(String csrf, String jwtToken, UserSession userSession) {
        // 向redis写入缓存
        stringRedisTemplate.boundValueOps(LsConstants.CSRF_PREFIX + csrf)
                .set("1", authProperties.getSignIn().getExpiration(), TimeUnit.SECONDS);

        jwtToken = jwtToken.replace(authProperties.getJwtToken().getPrefix() + " ", "");
        stringRedisTemplate
                .boundValueOps(LsConstants.SESSION_SUFFIX + jwtToken)
                .set(JSON.toJSONString(userSession), authProperties.getSignIn().getExpiration(), TimeUnit.SECONDS);
    }

}
