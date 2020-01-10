package cn.lishe.javabase.filter;

import cn.hutool.core.util.StrUtil;
import cn.lishe.javabase.define.LsConstants;
import cn.lishe.javabase.define.Re;
import cn.lishe.javabase.define.Response;
import cn.lishe.javabase.properites.AuthProperties;
import cn.lishe.javabase.properites.JwtTokenProperties;
import cn.lishe.javabase.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author YeJin
 * @date 2019/10/29 14:55
 */
@Component
public class SessionFilter extends OncePerRequestFilter {
    private static Logger lg = LoggerFactory.getLogger(SessionFilter.class);
    @Resource
    private AuthProperties mp;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (pass(request) || checkSession(request)) {
            filterChain.doFilter(request, response);
        } else {
            lg.warn("token参数已超时，请重新登录！");
            Response res = R.write(Re.EXPIRED_TOKEN);
            R.writeJsonResponse(res, response);
        }
    }

    private boolean checkSession(HttpServletRequest request) {
        JwtTokenProperties jwtToken = mp.getJwtToken();
        String token = request.getHeader(jwtToken.getHeaderName());
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(jwtToken.getRequestName());
        }
        boolean valid = false;
        if (StrUtil.isNotBlank(token)) {
            Boolean hasKey = stringRedisTemplate.hasKey(LsConstants.SESSION_SUFFIX + token);
            valid = hasKey != null ? hasKey : false;
        }
        return valid;
    }
    private boolean pass(HttpServletRequest req) {
        String uri = req.getRequestURI();
        List<String> exclusivePath = mp.getJwtToken().getExclusivePath();
        boolean pass = false;
        for (String path : exclusivePath) {
            AntPathMatcher matcher = new AntPathMatcher();
            if (matcher.match(path, uri)) {
                pass = true;
                break;
            }
        }
        return pass;
    }

}
