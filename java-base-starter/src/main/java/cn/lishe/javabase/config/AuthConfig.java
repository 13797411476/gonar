package cn.lishe.javabase.config;

import cn.lishe.javabase.filter.PermissionFilter;
import cn.lishe.javabase.filter.SessionFilter;
import cn.lishe.javabase.filter.SignTokenFilter;
import cn.lishe.javabase.filter.TokenFilter;
import cn.lishe.javabase.properites.AuthProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YeJin
 * @date 2019/10/29 16:30
 */

@EnableWebSecurity
@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private SignTokenFilter signTokenFilter;
    @Resource
    private PermissionFilter permissionFilter;
    @Resource
    private SessionFilter sessionFilter;
    @Resource
    private TokenFilter tokenFilter;
    @Resource
    private AuthProperties authProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> exclusivePath = authProperties.getJwtToken().getExclusivePath();
        String[] sArr = null;
        if (exclusivePath != null && !exclusivePath.isEmpty()) {
            sArr = new String[exclusivePath.size()];
            for (int i = 0; i < exclusivePath.size(); i++) {
                sArr[i] = exclusivePath.get(i);
            }
        }


        http
                .addFilterAfter(signTokenFilter, ChannelProcessingFilter.class)
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(sessionFilter, TokenFilter.class)
                .addFilterAfter(permissionFilter, SessionFilter.class)
                .csrf().disable()
                .headers().frameOptions().sameOrigin()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        if (sArr != null) {
            http.authorizeRequests().antMatchers(sArr).permitAll().anyRequest().authenticated();
        } else {
            http.authorizeRequests().anyRequest().authenticated();
        }
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
