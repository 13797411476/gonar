package cn.lishe.gateway.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum FuseEnum implements IEnum<Integer> {
    /**
     * 正常
     */
    normal(1),
    /**
     * 降级中
     */
    fusing(0);

    private int code;

    FuseEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}
