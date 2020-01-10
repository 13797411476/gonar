package com.gonar.dynamicdatasource.saas.dao;

import com.gonar.dynamicdatasource.config.mybatis.DBTypeEnum;
import com.gonar.dynamicdatasource.config.mybatis.DataSource;
import com.gonar.dynamicdatasource.saas.entity.Com;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 企业表 Mapper 接口
 * </p>
 *
 * @author YeJin
 * @since 2019-08-27
 */
@DataSource(DBTypeEnum.saasDataSource)
public interface ComMapper extends BaseMapper<Com> {

    Com selectById(String comId);

}
