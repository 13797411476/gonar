package cn.lishe.gateway;

import cn.lishe.gateway.cache.RequestCache;
import cn.lishe.gateway.core.GatewayServer;
import cn.lishe.gateway.entity.Router;
import cn.lishe.gateway.mapper.RouterMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.misc.Request;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YeJin
 * @date 2020/1/9 14:51
 */
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    @Resource
    private GatewayServer gatewayServer;
    @Resource
    private RouterMapper routerMapper;


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        init();
        gatewayServer.start();
    }

    private void init() {
        List<Router> routers = routerMapper.selectList(null);
        routers.forEach(router -> RequestCache.routerMap.put(router.getPath().toLowerCase(), router));
    }


}
