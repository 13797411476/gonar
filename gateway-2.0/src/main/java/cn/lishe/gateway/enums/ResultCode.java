package cn.lishe.gateway.enums;

public enum ResultCode {
    /**
     * 成功
     */
    success(0, "success"),
    /**
     * 降级中
     */
    reducing(1, "reducing"),
    /**
     * 熔断中
     */
    fusing(2, "fusing"),

    /**
     * http 转发错误
     */
    http_proxy_error(3, "http_proxy_error"),
    /**
     * http 请求方法不支持
     */
    http_method_not_support(4, "http method not support"),

    http_redirect(5, "重定向请求"),

    request_limit(6, "请求限流")
    ;


    private int code;
    private String msg;


    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
