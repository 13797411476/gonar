package cn.lishe.gateway.entity;

import cn.lishe.gateway.enums.FuseEnum;
import cn.lishe.gateway.enums.ReduceEnum;
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
    private ReduceEnum reduce;
    /**
     * 1-正常 0-熔断中
     */
    private FuseEnum fuse;
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

    public ReduceEnum getReduce() {
        return reduce;
    }

    public void setReduce(ReduceEnum reduce) {
        this.reduce = reduce;
    }

    public FuseEnum getFuse() {
        return fuse;
    }

    public void setFuse(FuseEnum fuse) {
        this.fuse = fuse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
