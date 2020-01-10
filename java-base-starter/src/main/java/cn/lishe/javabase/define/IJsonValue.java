package cn.lishe.javabase.define;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author YeJin
 * @date 2019/11/27 16:35
 */
public interface IJsonValue<T> {

    /**
     * json序列化时返回的值
     *
     * @return T
     */
    @JsonValue
    T jsonValue();
}
