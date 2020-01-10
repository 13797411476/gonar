package com.gonar.dynamicdatasource.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author YeJin
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_admin_user")
public class AdminUser implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 1-启用，0-禁用
     */
    private String status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 错误登录次数
     */
    private Integer loginErrorNum;

    /**
     * 上次登录错误时间
     */
    private Date loginErrorTime;

    /**
     * 登录时间
     */
    private Date loginTime;

    private Date createTime;

    private Date updateTime;


}
