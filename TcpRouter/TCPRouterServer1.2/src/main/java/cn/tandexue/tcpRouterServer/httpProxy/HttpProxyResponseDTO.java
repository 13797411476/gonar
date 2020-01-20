package cn.tandexue.tcpRouterServer.httpProxy;

public class HttpProxyResponseDTO {
    private int errorCode;
    private String errorMsg;
    private String content;

    @Override
    public String toString() {
        return "HttpProxyResponseDTO{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
