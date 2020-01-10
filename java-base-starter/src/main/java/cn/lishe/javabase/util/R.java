package cn.lishe.javabase.util;

import cn.hutool.core.bean.BeanUtil;
import cn.lishe.javabase.define.Re;
import cn.lishe.javabase.define.Response;
import cn.lishe.javabase.define.ResultCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * @author YeJin
 * @date 2019/10/29 9:23
 */
public class R {
private static Logger lg = LoggerFactory.getLogger(R.class);
    /**
     * 返回Json回应给客户端
     *
     * @param rsp     回应信息
     * @param httpRsp HttpServletResponse
     */
    public static void writeJsonResponse(Response rsp, HttpServletResponse httpRsp) {
        String json = JSON.toJSONString(rsp,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNonStringValueAsString);
        lg.debug("JSON:\r\n{}", json);

        try {
            httpRsp.setContentType("application/json;charset=utf-8");
            httpRsp.setStatus(HttpServletResponse.SC_OK);
            httpRsp.getWriter().write(json);
            httpRsp.getWriter().flush();
        } catch (Exception e) {
            lg.error("Response Json回写失败：", e);
        }
    }

    public static <T> T copy(Object source, T t) {
        if(source != null) {
            BeanUtil.copyProperties(source, t);
        }
        return t;
    }

    public static Response ok() {
        return Response.builder().code(Re.SUCC.getCode()).msg(Re.SUCC.getMsg()).build();
    }

    public static Response ok(Object data) {
        return Response.builder().code(Re.SUCC.getCode()).msg(Re.SUCC.getMsg()).data(data).build();
    }

    public static Response error() {
        return Response.builder().code(Re.ERROR.getCode()).msg(Re.ERROR.getMsg()).build();
    }

    public static Response write(ResultCode re) {
        return Response.builder().code(re.getCode()).msg(re.getMsg()).build();
    }
    public static Response write(String msg, Re re) {
        return Response.builder().code(re.getCode()).msg(msg).build();
    }

    public static Response write(ResultCode re, Object data) {
        return Response.builder().code(re.getCode()).msg(re.getMsg()).data(data).build();
    }
}
