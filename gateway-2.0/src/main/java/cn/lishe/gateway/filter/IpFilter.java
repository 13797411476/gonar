package cn.lishe.gateway.filter;

import cn.lishe.gateway.core.GatewayContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author YeJin
 * @date 2020/1/9 16:31
 */
@Component
@Order
public class IpFilter implements GatewayRouterFilter {


    @Override
    public void filter(GatewayContext context, GatewayRouterFilterChain chain) {
        InetSocketAddress socketAddress = (InetSocketAddress) context.getCtx().channel().remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        System.out.println(hostAddress);

        chain.filter(context);
    }

    @Override
    public int order() {
        return 0;
    }


}
