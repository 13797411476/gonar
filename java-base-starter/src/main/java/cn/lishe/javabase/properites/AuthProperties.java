package cn.lishe.javabase.properites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * @author YeJin
 * @date 2019/10/28 16:51
 */
@Configuration
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {

    @NestedConfigurationProperty
    private JwtTokenProperties jwtToken = new JwtTokenProperties();

    @NestedConfigurationProperty
    private SignIn signIn = new SignIn();

    @NestedConfigurationProperty
    private SignToken signToken = new SignToken();

    @NestedConfigurationProperty
    private Permission permission = new Permission();

    @Data
    public static class SignToken {
        private String headerName = "token";
        private String requestName = "token";
        private String secret = "lishe_md5_key_56e057f20f883e";
        private List<String> includePath = Collections.emptyList();

    }
    @Data
    public static class Permission {
        private boolean check = true;
    }

    @Data
    public static class SignIn {

        /**
         * default : locked
         */
        private boolean lock = true;

        private int maxErrorTimes = 3;

        /**
         * default : 5 minutes
         */
        private long lockedSeconds = 300;

        /**
         * default : 30 minutes
         */
        private long expiration = 1800;

    }
}
