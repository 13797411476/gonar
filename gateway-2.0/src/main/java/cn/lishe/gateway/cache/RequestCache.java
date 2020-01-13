package cn.lishe.gateway.cache;

import cn.lishe.gateway.entity.Router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestCache {

    public static Map<String, Router> routerMap = new ConcurrentHashMap<>(8);
    public static Map<String, Long> reduingMap = new ConcurrentHashMap<>(8);

    public static Router getRouter(String uri) {
        if (uri.startsWith("/")) {
            return RequestCache.routerMap.get(uri.toLowerCase().substring(1));
        }
        return RequestCache.routerMap.get(uri.toLowerCase());
    }

}
