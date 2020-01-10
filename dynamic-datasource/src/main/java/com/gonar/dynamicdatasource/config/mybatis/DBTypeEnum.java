package com.gonar.dynamicdatasource.config.mybatis;

/**
 * @author Connor
 * @date 2019/5/14 16:36
 */
public enum DBTypeEnum {
    /**
     * shop
     */
    shopDataSource("shopDataSource"),

    /**
     * saas
     */
    saasDataSource("saasDataSource"),

    /**
     * crm
     */
    crmDataSource("crmDataSource");

    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
