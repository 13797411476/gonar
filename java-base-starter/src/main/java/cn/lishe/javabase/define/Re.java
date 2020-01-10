package cn.lishe.javabase.define;

/**
 * @author YeJin
 * @date 2019/10/29 9:35
 */
public enum Re implements ResultCode {
    /**
     * 返回枚举
     */
    SUCC(200, "SUCCESS"),
    ERROR(500, "ERROR"),

    /**
     * 用户相关
     */
    INVALID_JWT_TOKEN(100002, "token校验失败"),
    INVALID_SIGN_TOKEN(100003, "sign签名校验失败"),
    DENY_AUTH(100004, "权限不足"),
    LOGIN_FAILURE(100005, "登录失败"),
    EXPIRED_TOKEN(100006, "token已失效"),


    PARAM_ERROR(200001, "参数错误")
    ;

    private int code;
    private String msg;

    Re(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
