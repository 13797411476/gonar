package cn.lishe.javabase.properites;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YeJin
 * @date 2019/10/28 16:56
 */
@Data
public class JwtTokenProperties {

    private String headerName = "Authorization";
    private String requestName = "authorization";
    private String secret = "lsidk210398$od$12so";
    private String prefix = "Bearer";

    private List<String> exclusivePath = new ArrayList<>(8);
}
