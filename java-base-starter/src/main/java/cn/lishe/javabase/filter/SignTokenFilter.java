package cn.lishe.javabase.filter;

import cn.hutool.core.util.StrUtil;
import cn.lishe.javabase.define.Ctx;
import cn.lishe.javabase.define.Re;
import cn.lishe.javabase.define.SignGenerator;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YeJin
 * @date 2019/10/29 11:13
 */
@Component
public class SignTokenFilter extends OncePerRequestFilter {
    private static Logger lg = LoggerFactory.getLogger(SignTokenFilter.class);
    @Resource
    private AuthProperties authProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (pass(request) || checkSign(request)) {
            filterChain.doFilter(request, response);
        } else {
            lg.info("sign签名失败:{}", request.getRequestURI());
            R.writeJsonResponse(R.write(Re.INVALID_SIGN_TOKEN), response);
        }
    }

    private boolean checkSign(HttpServletRequest request) {
        AuthProperties.SignToken signToken = authProperties.getSignToken();
        String sign = request.getHeader(signToken.getHeaderName());
        if (StrUtil.isBlank(sign)) {
            sign = request.getParameter(signToken.getRequestName());
        }

        Map<String, Object> map = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            if (signToken.getRequestName().equalsIgnoreCase(name)) {
                continue;
            }
            String value = request.getParameter(name);
            map.put(name, value);
        }

        SignGenerator signGenerator = Ctx.CONTEXT.getBean(SignGenerator.class);
        return signGenerator.verify(map, sign);
    }

    private boolean pass(HttpServletRequest req) {
        String uri = req.getRequestURI();
        List<String> includePath = authProperties.getSignToken().getIncludePath();
        boolean pass = true;
        for (String path : includePath) {
            AntPathMatcher matcher = new AntPathMatcher();
            if (matcher.match(path, uri)) {
                pass = false;
                break;
            }
        }
        return pass;
    }


}
