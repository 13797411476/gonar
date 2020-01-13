package cn.lishe.gateway.enums;

public enum FuseEnum {
    /**
     * 正常
     */
    normal(0),
    /**
     * 降级中
     */
    fusing(1);

    private int code;

    FuseEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
