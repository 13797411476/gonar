package cn.lishe.javabase.define;

import cn.hutool.core.util.StrUtil;
import cn.lishe.javabase.properites.AuthProperties;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author YeJin
 * @date 2019/11/4 9:03
 */
@Data
public class UserSession {

    /**
     * jwt Token
     */
    private String token;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 资源url集合
     */
    private Set<String> urls = new HashSet<>();

    public static void saveUserSession(String jwtToken, UserSession userSession) {
        StringRedisTemplate stringRedisTemplate = Ctx.CONTEXT.getBean(StringRedisTemplate.class);
        AuthProperties authProperties = Ctx.CONTEXT.getBean(AuthProperties.class);
        stringRedisTemplate
                .boundValueOps(LsConstants.SESSION_SUFFIX + jwtToken)
                .set(JSON.toJSONString(userSession), authProperties.getSignIn().getExpiration(), TimeUnit.SECONDS);
    }

    public static UserSession getUserSession() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null) {
            return null;
        }
        HttpServletRequest req = requestAttributes.getRequest();

        AuthProperties authProperties = Ctx.CONTEXT.getBean(AuthProperties.class);

        String jwtToken = req.getHeader(authProperties.getJwtToken().getHeaderName());
        if (StrUtil.isBlank(jwtToken)) {
            jwtToken = req.getParameter(authProperties.getJwtToken().getRequestName());
        }

        UserSession userSession = new UserSession();
        if (StrUtil.isNotBlank(jwtToken)) {
            StringRedisTemplate stringRedisTemplate = Ctx.CONTEXT.getBean(StringRedisTemplate.class);
            String userSessionString = stringRedisTemplate.boundValueOps(LsConstants.SESSION_SUFFIX + jwtToken).get();
            userSession = JSON.parseObject(userSessionString, UserSession.class);
        }

        return userSession;
    }

}
