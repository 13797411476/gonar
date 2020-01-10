package com.gonar.dynamicdatasource.config.mybatis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.gonar.dynamicdatasource.config.handler.LocalDateHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YeJin
 * @date 2019/8/27 16:50
 */
@Configuration
public class MybatisPlusConfig {
    @Bean(name = "shopDataSource")
    @ConfigurationProperties(prefix = "dynamic.datasource.shop" )
    public DataSource shop() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "saasDataSource")
    @ConfigurationProperties(prefix = "dynamic.datasource.saas" )
    public DataSource saas() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "crmDataSource")
    @ConfigurationProperties(prefix = "dynamic.datasource.crm" )
    public DataSource crm() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    /**
     * 动态数据源配置
     */
    @Bean
    @Primary
    public DataSource multipleDataSource (@Qualifier("shopDataSource") DataSource shop,
                                          @Qualifier("saasDataSource") DataSource saas,
                                          @Qualifier("crmDataSource") DataSource crm ) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(8);
        targetDataSources.put(DBTypeEnum.shopDataSource.getValue(), shop );
        targetDataSources.put(DBTypeEnum.saasDataSource.getValue(), saas);
        targetDataSources.put(DBTypeEnum.crmDataSource.getValue(), crm);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(crm);
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(shop(), saas(), crm()));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{ paginationInterceptor()});
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/*/*.xml");
        sqlSessionFactory.setMapperLocations(resources);
//        sqlSessionFactory.setTypeHandlersPackage("com.gonar.dynamicdatasource.config.handler");
        TypeHandler[] handler = new TypeHandler[]{new LocalDateHandler()};
        sqlSessionFactory.setTypeHandlers(handler);
        return sqlSessionFactory.getObject();
    }

}
