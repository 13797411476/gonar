package cn.lishe.gateway;

import cn.lishe.gateway.core.GatewayServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author YeJin
 * @date 2020/1/9 14:51
 */
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    @Autowired
    private GatewayServer gatewayServer;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        gatewayServer.start();
    }
}
