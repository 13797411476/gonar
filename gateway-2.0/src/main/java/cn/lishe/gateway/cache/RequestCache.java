package cn.lishe.gateway.cache;

import cn.lishe.gateway.entity.Router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestCache {

    public static Map<String, Router> routerMap = new ConcurrentHashMap<>(8);


}
