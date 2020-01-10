package cn.lishe.javabase.util;

import cn.hutool.core.util.RandomUtil;
import cn.lishe.javabase.properites.AuthProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtTokenUtils
 *
 * @author YeJin
 * @date 2019/10/25 14:37
 */
@Component
public class JwtTokenUtils {

    private static final Logger lg = LoggerFactory.getLogger(JwtTokenUtils.class);

    private static AuthProperties authProperties;

    /**
     * 生成token
     *
     * @param claim claim
     * @return token
     */
    public static String createToken(Map<String, Object> claim) {
        LocalDateTime expireLocalDateTime = LocalDateTime.now().plus(authProperties.getSignIn().getExpiration(), ChronoUnit.SECONDS);

        String jwtPrefix = authProperties.getJwtToken().getPrefix();
        String jwtToken = Jwts.builder()
                .setClaims(claim)
                .setExpiration(DateUtils.localDateTimeToDate(expireLocalDateTime))
                .signWith(SignatureAlgorithm.HS512, authProperties.getJwtToken().getSecret())
                .compact();
        return jwtPrefix + " " + jwtToken;
    }

    /**
     * 根据用户信息生成口令
     * <p>注意： 不能嵌套放置，否则无法解析</p>
     */
    public static String createToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("token", RandomUtil.randomString(12));
        return createToken(claims);
    }

    /**
     * 根据token得到数据声明
     *
     * @param token token
     * @return result
     */
    public static Map<String, Object> getClaimsFromToken(String token) {
        String realToken = token.replace(authProperties.getJwtToken().getPrefix() + " ", "");

        return Jwts.parser()
                .setSigningKey(authProperties.getJwtToken().getSecret())
                .parseClaimsJws(realToken)
                .getBody();
    }

    /**
     * 解析用户
     *
     * @param token token
     * @return result
     */
    public static String getUsernameFromToken(String token) {
        String username;
        try {
            username = ((String) getClaimsFromToken(token).get("username"));
        } catch (Exception e) {
            username = null;
            lg.error("JWT Error :", e);
        }
        return username;
    }

    /**
     * 判断口令是否过期
     *
     * @param token token
     * @return result
     */
    public static boolean isExpired(String token) {
        boolean isExpired = false;
        try {
            JwtTokenUtils.getClaimsFromToken(token);
        } catch (Exception e) {
            isExpired = true;
            lg.error("JWT expired error :", e);
        }
        return isExpired;
    }

    /**
     * 刷新口令
     *
     * @param token token
     * @return token
     */
    public static String refreshToken(String token) {
        String reToken = null;
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            reToken = createToken(claims);
        } catch (Exception e) {
            lg.error("刷新token失败", e);
        }
        return reToken;
    }

    /**
     * 验证口令
     *
     * @param token       token
     * @param userDetails userDetails
     * @return result
     */
    public static boolean valid(String token, UserDetails userDetails) {
        if (null == token || null == userDetails) {
            return false;
        }

        String loginName = getUsernameFromToken(token);
        // failure
        if (loginName == null) {
            return false;
        }
        return loginName.equals(userDetails.getUsername()) && !isExpired(token);
    }

    @Autowired
    public void setProjectProperties(AuthProperties authProperties) {
        JwtTokenUtils.authProperties = authProperties;
    }

}
