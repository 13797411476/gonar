package cn.lishe.gateway.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author YeJin
 * @date 2020/1/9 14:57
 */
@Configuration
@EnableConfigurationProperties(NettyConfig.class)
public class PropertiesConfig {

}
