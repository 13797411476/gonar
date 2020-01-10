package cn.lishe.javabase.config;

import cn.lishe.javabase.properites.DbProperties;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author YeJin
 * @date 2019/11/27 15:52
 */
@Configuration
public class DataSourceConfig {

    @Resource
    private DbProperties dbProperties;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbProperties.getUrl());
        datasource.setUsername(dbProperties.getUsername());
        datasource.setPassword(dbProperties.getPassword());
        datasource.setDriverClassName(dbProperties.getDriverClassName());
        return datasource;
    }

}
