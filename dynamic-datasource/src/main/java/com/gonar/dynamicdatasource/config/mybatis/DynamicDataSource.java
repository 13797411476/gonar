package com.gonar.dynamicdatasource.config.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author YeJin
 * @date 2019/8/27 16:53
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }

    public static void setDataSource(DBTypeEnum dbTypeEnum) {
        DbContextHolder.setDbType(dbTypeEnum);
    }

    public static String getDataSource() {
        return DbContextHolder.getDbType();
    }

    public static void clearDataSource() {
        DbContextHolder.clearDbType();
    }
}
