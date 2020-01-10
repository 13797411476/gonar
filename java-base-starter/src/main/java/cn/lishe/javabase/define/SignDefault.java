package cn.lishe.javabase.define;

import cn.lishe.javabase.properites.AuthProperties;
import cn.lishe.javabase.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author YeJin
 * @date 2019/11/25 10:09
 */
@Slf4j
@Component
@ConditionalOnMissingBean(SignGenerator.class)
public class SignDefault implements SignGenerator<Map<String, Object>> {

    @Autowired
    private AuthProperties authProperties;


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private String createSignString(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            //拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }
        log.info("排序后的参数串:[{}]", prestr.toString());
        return prestr.toString();
    }


    /**
     * 生成签名结果
     *
     * @param map 要签名的数组
     * @return 签名结果字符串
     */
    private String buildSign(Map<String, Object> map) {
        String md5 = Md5Util.md5(createSignString(map) + authProperties.getSignToken().getSecret());
        log.info("md5结果：{}", md5);
        return md5;
    }

    @Override
    public String create(Map<String, Object> o) {
        return buildSign(o);
    }

    /**
     * @param map  要签名的数组
     * @param sign 签名结果字符串
     * @return 验sign结果
     */
    @Override
    public boolean verify(Map<String, Object> map, String sign) {
        log.info("入参sign：{}", sign);
        return buildSign(map).equals(sign);
    }

}
