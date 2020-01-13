package cn.lishe.gateway.http;

import cn.lishe.gateway.conf.HeaderConfig;
import cn.lishe.gateway.response.RespDTO;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YeJin
 * @date 2019/11/12 16:15
 */
@Component
public class ProxyGetRequest {
    private Pattern pattern = Pattern.compile("(.*)\\(([\\{\\[].*[\\}\\]])\\)$");
    private static final String CALLBACK = "callback";
    @Resource
    private ProxySendRequest proxySendRequest;

    public RespDTO doGet(String realUrl, FullHttpRequest request) {
        HttpHeaders headers = request.headers();

        String[] urlAndQuery = request.uri().split("\\?");
        String queryString = "";
        if (urlAndQuery.length > 1) {
            queryString = urlAndQuery[1];
        }
        String finallyUrl = "";
        String callbackMethod = null;
        if (!"".equals(queryString)) {
            finallyUrl = realUrl + "?" + queryString;
            callbackMethod = isJsonp(queryString);
        } else {
            finallyUrl = realUrl;
        }

        HttpGet httpGet = new HttpGet(finallyUrl);
        headers.forEach(header -> {
            if(!HeaderConfig.ignoreHeaders.contains(header.getKey())) {
                httpGet.addHeader(header.getKey(), header.getValue());
            }

        });
        RespDTO respDTO = proxySendRequest.execute(httpGet);
        return dealJsonp(respDTO, callbackMethod);
    }


    /**
     * jsonp结果处理
     *
     * @param respDTO        接口返回实体
     * @param callbackMethod jsonp回调方法名
     * @return jsonp处理后返回实体
     */
    private RespDTO dealJsonp(RespDTO respDTO, String callbackMethod) {
        if (respDTO.getStatus() != 0) {
            return respDTO;
        }
        byte[] contentByte = respDTO.getContent();
        if (contentByte == null) {
            return respDTO;
        }

        String content = new String(contentByte);
        if (!StringUtils.isEmpty(content)) {
            //数据非空
            if (null != callbackMethod) {
                // 请求是jsonp 若结果不是jsonp的格式,进行转换
                if (!(content.startsWith(callbackMethod + "(") && content.endsWith(")"))) {
                    respDTO.setContent((callbackMethod + "(" + content + ")").getBytes());
                    return respDTO;
                }
            } else {
                // 请求不是jsonp, 若结果是jsonp的格式,进行转换
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    respDTO.setContent(matcher.group(2).getBytes());
                    return respDTO;
                }
            }
        }
        //数据为空，直接返回
        return respDTO;
    }

    /**
     * 判断是否是jsonp调用, 是的话,返回回调方法名
     *
     * @param queryString 参数
     * @return 回调方法名
     */
    private String isJsonp(String queryString) {
        String[] nameValuePairs = queryString.split("&");
        if (nameValuePairs.length > 0) {
            for (String nameValuePair : nameValuePairs) {
                String[] split = nameValuePair.split("=");
                if (split.length > 1) {
                    String name = split[0];
                    if (CALLBACK.equalsIgnoreCase(name)) {
                        return split[1];
                    }
                }
            }
        }
        return null;
    }


}
