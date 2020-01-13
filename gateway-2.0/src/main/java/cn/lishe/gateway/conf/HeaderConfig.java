package cn.lishe.gateway.conf;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YeJin
 * @date 2019/11/13 14:43
 */
public class HeaderConfig {
    public static Set<String> ignoreHeaders;
    static {
        ignoreHeaders = new HashSet<>(3);
        ignoreHeaders.add("Host");
        ignoreHeaders.add("Content-Length");
        ignoreHeaders.add("Content-Type");
    }

}
