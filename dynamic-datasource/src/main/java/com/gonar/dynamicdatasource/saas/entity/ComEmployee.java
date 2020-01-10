package com.gonar.dynamicdatasource.saas.entity;

import java.math.BigDecimal;
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
@TableName("t_com_employee")
public class ComEmployee implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 企业ID
     */
    private String comId;

    /**
     * 所在公司
     */
    private String comName;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登陆密码
     */
    private String loginPwd;

    /**
     * 员工名字
     */
    private String empName;

    /**
     * 职位
     */
    private Integer posId;

    /**
     * 工号
     */
    private String empNo;

    /**
     * 状态 [-1删除状态 0 未激活,1已激活]
     */
    private String status;

    /**
     * 上次登录时间
     */
    private Date loginTime;

    /**
     * 错误登陆时间
     */
    private Date loginErrTime;

    /**
     * 密码错误次数
     */
    private Integer loginErrNum;

    /**
     * 部门ID
     */
    private Integer departId;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 生日时间
     */
    private Date birthDate;

    /**
     * 分组Id
     */
    private Integer groupId;

    /**
     * 可用积分
     */
    private BigDecimal balance;

    /**
     * 已用积分
     */
    private BigDecimal freezeAmount;

    /**
     * 通用积分
     */
    private BigDecimal commonAmount;

    /**
     * 限制积分
     */
    private BigDecimal limitAmount;

    /**
     * 扩展信息
     */
    private String extendInfo;

    /**
     * 扩展信息2
     */
    private String extendInfoTwo;

    /**
     * 数据版本号
     */
    private Integer version;

    /**
     * 注册时间
     */
    private Date regTime;

    /**
     * 注册渠道
     */
    private String regSource;

    private Date entryDate;

    private String gender;

    /**
     * 用户头像路径
     */
    private String userAvatarPath;


}
