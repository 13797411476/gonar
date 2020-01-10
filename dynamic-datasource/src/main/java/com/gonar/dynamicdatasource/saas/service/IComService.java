package com.gonar.dynamicdatasource.saas.service;

import com.gonar.dynamicdatasource.saas.entity.Com;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 企业表 服务类
 * </p>
 *
 * @author YeJin
 * @since 2019-08-27
 */
public interface IComService extends IService<Com> {

    Object selectById(String comId);

}
