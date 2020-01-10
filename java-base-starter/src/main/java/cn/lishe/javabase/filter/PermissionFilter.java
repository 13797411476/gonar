package cn.lishe.javabase.filter;

import cn.hutool.core.util.StrUtil;
import cn.lishe.javabase.define.Re;
import cn.lishe.javabase.define.UserSession;
import cn.lishe.javabase.properites.AuthProperties;
import cn.lishe.javabase.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Set;

/**
 * @author YeJin
 * @date 2019/10/29 15:07
 */
@Component
public class PermissionFilter extends OncePerRequestFilter {
    private static Logger lg = LoggerFactory.getLogger(PermissionFilter.class);
    @Resource
    private AuthProperties authProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (pass(request) || hasPermission(request)) {
            filterChain.doFilter(request, response);
        } else {
            lg.warn("权限不足");
            R.writeJsonResponse(R.write(Re.DENY_AUTH), response);
        }
    }

    private boolean hasPermission(HttpServletRequest request) {

        AuthProperties.Permission permission = authProperties.getPermission();
        if(!permission.isCheck()) {
            return true;
        }
        boolean hasPermission = false;

        UserSession userSession = UserSession.getUserSession();
        if (userSession != null) {
            Set<String> urls = userSession.getUrls();
            AntPathMatcher pathMatcher = new AntPathMatcher();
            String requestUri = request.getRequestURI();
            for (String url : urls) {
                if (StrUtil.isNotBlank(url) && pathMatcher.match(url, requestUri)) {
                    hasPermission = true;
                    break;
                }
            }
        }

        return hasPermission;
    }

    private boolean pass(HttpServletRequest req) {
        String uri = req.getRequestURI();
        List<String> exclusivePath = authProperties.getJwtToken().getExclusivePath();
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
