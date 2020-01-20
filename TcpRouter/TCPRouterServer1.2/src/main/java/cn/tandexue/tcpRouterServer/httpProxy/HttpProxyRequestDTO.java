package cn.tandexue.tcpRouterServer.httpProxy;

public class HttpProxyRequestDTO {

    private String url;
    private String param;
    private String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "HttpProxyRequestDTO{" +
                "url='" + url + '\'' +
                ", param='" + param + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
