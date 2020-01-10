package com.gonar.dynamicdatasource.config.mybatis;

import java.lang.annotation.*;

/**
 * @author Connor
 * @date 2019/5/14 18:02
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    DBTypeEnum value() default DBTypeEnum.shopDataSource;

}
