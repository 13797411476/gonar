package cn.lishe.gateway.enums;

public enum  ReduceEnum {
    /**
     * 正常
     */
    normal(0),
    /**
     * 降级中
     */
    reducing(1);

    private int code;

    ReduceEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
