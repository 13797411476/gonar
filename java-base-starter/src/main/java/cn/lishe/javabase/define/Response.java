package cn.lishe.javabase.define;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;

/**
 * 此类不许修改
 *
 * @author YeJin
 * @date 2019/7/16 9:57
 */
@Getter
@Builder
public class Response {
    public interface ResultInter {}

    @JsonView(ResultInter.class)
    private int code;
    @JsonView(ResultInter.class)
    private String msg;
    @JsonView(ResultInter.class)
    private Object data;


}
