package cn.lishe.gateway.response;

public class CommonResponseConfig {
    public static final String PATH_ERROR = "{\"result\":200,\"errcode\":200001,\"msg\":\"系统错误(api not found)\",\"data\":\"\"}";
    public static final String DECODE_ERROR = "{\"result\":200,\"errcode\":200002,\"msg\":\"系统错误(request decode error)\",\"data\":\"\"}";
    public static final String COMMAND_OK = "{\"result\":100,\"errcode\":0,\"msg\":\"execute ok (%d)\",\"data\":\"\"}";
    public static final String UNSURPPORT_METHOD = "{\"result\":200,\"errcode\":200003,\"msg\":\"系统错误(unsupport method)\",\"data\":\"\"}";
    public static final String TIME_OUT = "{\"result\":200,\"errcode\":200004,\"msg\":\"系统繁忙，请稍后再试(timeout)\",\"data\":\"\"}";
    public static final String API_ERROR = "{\"result\":200,\"errcode\":200005,\"msg\":\"系统繁忙，请稍后再试(error)\",\"data\":\"\"}";
    public static final String DEFAULT_VALUE = "{\"result\":200,\"errcode\":200006,\"msg\":\"系统繁忙，请稍后再试(default)\",\"data\":\"\"}";

    public static RespDTO getRespDto(String string) {
        RespDTO respDTO = new RespDTO();
        respDTO.setContent(string.getBytes());
        return respDTO;
    }

}
