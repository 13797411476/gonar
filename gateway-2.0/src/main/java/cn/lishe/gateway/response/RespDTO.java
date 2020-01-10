package cn.lishe.gateway.response;

import java.util.Map;

public class RespDTO {
    private long useTime;
    /**
        0:nomal
        1:timeout
        2:error
     */
    private int status;
    private Map<String,String> headers;
    private int code;
    private String msg;
    private byte[] content;

    public static RespDTO error(String msg) {
        RespDTO respDTO = new RespDTO();
        respDTO.setCode(2);
        respDTO.setMsg(msg);
        respDTO.setContent(CommonResponseConfig.DEFAULT_VALUE.getBytes());
        return respDTO;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }


}
