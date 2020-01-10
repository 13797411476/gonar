package cn.lishe.javabase.exception;

import cn.lishe.javabase.define.ResultCode;

/**
 * 业务异常
 * @author Connor
 * @date 2019/5/13 15:54 
 */
public class BusinessException extends RuntimeException {

    private final transient ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
