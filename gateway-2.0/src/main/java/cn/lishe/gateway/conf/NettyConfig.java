
package cn.lishe.gateway.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
