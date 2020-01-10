package cn.lishe.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableId;

public class Router {
    @TableId
    private Integer routerId;
    private String name;
    private String path;
    private String url;
    private Integer healthy;
    /**
     * 1-正常 0-降级中
     */
    private Integer reduce;
    /**
     * 1-正常 0-熔断中
     */
    private Integer fuse;
    private String remark;

    public Integer getRouterId() {
        return routerId;
    }

    public void setRouterId(Integer routerId) {
        this.routerId = routerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHealthy() {
        return healthy;
    }

    public void setHealthy(Integer healthy) {
        this.healthy = healthy;
    }

    public Integer getReduce() {
        return reduce;
    }

    public void setReduce(Integer reduce) {
        this.reduce = reduce;
    }

    public Integer getFuse() {
        return fuse;
    }

    public void setFuse(Integer fuse) {
        this.fuse = fuse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
