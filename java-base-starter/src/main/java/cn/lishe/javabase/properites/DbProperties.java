package cn.lishe.javabase.properites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author YeJin
 * @date 2019/11/27 15:56
 */
@Configuration
@ConfigurationProperties(prefix = "db")
@Data
public class DbProperties {
    private String url;

    private String username;

    private String password;

    private String driverClassName;
}
