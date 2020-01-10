package cn.lishe.javabase.define;

/**
 * @author YeJin
 * @date 2019/11/27 17:41
 */
public interface SignGenerator<T> {
    String create(T o);

    boolean verify(T o, String sign);
}
