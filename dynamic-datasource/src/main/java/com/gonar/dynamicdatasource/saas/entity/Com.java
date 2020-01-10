package com.gonar.dynamicdatasource.saas.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 企业表
 * </p>
 *
 * @author YeJin
 * @since 2019-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_com")
public class Com implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 公司ID
     */
    @TableId
    private String comId;

    /**
     * 公司名
     */
    private String comName;

    /**
     * 申请人姓名
     */
    private String regName;

    /**
     * 申请人手机号
     */
    private String regPhoneNum;

    /**
     * 申请人邮箱
     */
    private String regEmail;

    /**
     * 注册时间
     */
    private LocalDateTime regTime;

    /**
     * 行业类型
     */
    private Long industryType;

    /**
     * 所在省份
     */
    private String provice;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在区
     */
    private String area;

    /**
     * 公司详细地址
     */
    private String address;

    /**
     * 法人代表
     */
    private String corpName;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 冻结资金
     */
    private BigDecimal freezeAmount;

    /**
     * 企业暗号
     */
    private String signNo;

    /**
     * 法人身份证号码
     */
    private String corpIdNum;

    /**
     * 营业执照
     */
    private String comLicenceImg;

    /**
     * 营业注册号
     */
    private String comRegNum;

    /**
     * 企业logo
     */
    private String logoImg;

    /**
     * 状态（0：待认证，1：已认证，2：认证不通过，9：未提交认证）
     */
    private String status;

    /**
     * 审核意见
     */
    private String checkMemo;

    /**
     * 审核提交时间
     */
    private Date checkSubTime;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * 审核用户名
     */
    private String checkUser;

    /**
     * 注销时间
     */
    private Date cancelTime;

    /**
     * 公司电话
     */
    private String comTelNum;

    private Integer version;

    /**
     * 激活发送积分关联ID
     */
    private Integer dataBaseId;

    /**
     * 客户经理关联ID
     */
    private Integer dataBaseId1;

    /**
     * 毛利率
     */
    private BigDecimal profitRate;

    /**
     * 公司二级域名
     */
    private String comDomain;

    /**
     * 注册渠道名称
     */
    private String channelName;

    /**
     * 渠道参数
     */
    private String channelParam;

    /**
     * 礼舍币余额
     */
    private Integer lsbBalance;

    /**
     * 已使用礼舍币
     */
    private Integer lsbUse;

    /**
     * 企业简称
     */
    private String nameAbbreviation;


}
