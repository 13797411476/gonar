package cn.lishe.javabase.filter;

import cn.hutool.core.util.StrUtil;
import cn.lishe.javabase.define.Re;
import cn.lishe.javabase.properites.AuthProperties;
import cn.lishe.javabase.properites.JwtTokenProperties;
import cn.lishe.javabase.util.JwtTokenUtils;
import cn.lishe.javabase.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
 * @date 2019/10/29 11:13
 */
@Component
public class TokenFilter extends OncePerRequestFilter {
    private static Logger lg = LoggerFactory.getLogger(TokenFilter.class);
    @Resource
    private AuthProperties mp;
    @Resource
    private UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (pass(request) || checkToken(request)) {
            filterChain.doFilter(request, response);
        } else {
            lg.info("无效的Authorization参数:{}", request.getRequestURI());
            R.writeJsonResponse(R.write(Re.INVALID_JWT_TOKEN), response);
        }
    }

    private boolean checkToken(HttpServletRequest request) {
        JwtTokenProperties jwtToken = mp.getJwtToken();
        String token = request.getHeader(jwtToken.getHeaderName());
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(jwtToken.getRequestName());
        }

        boolean valid = false;
        if (StrUtil.isNotBlank(token)) {
            String username = JwtTokenUtils.getUsernameFromToken(token);

            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                if (JwtTokenUtils.valid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    valid = true;
                }
            }
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
