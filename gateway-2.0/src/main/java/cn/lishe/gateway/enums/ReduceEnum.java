package cn.lishe.gateway.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum ReduceEnum implements IEnum<Integer> {
    /**
     * 正常
     */
    normal(1),
    /**
     * 降级中
     */
    reducing(0);

    private int code;

    ReduceEnum(int code) {
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
