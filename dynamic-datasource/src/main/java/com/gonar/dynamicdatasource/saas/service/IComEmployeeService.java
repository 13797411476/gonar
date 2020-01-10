package com.gonar.dynamicdatasource.saas.service;

import com.gonar.dynamicdatasource.saas.entity.ComEmployee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YeJin
 * @since 2019-08-28
 */
public interface IComEmployeeService extends IService<ComEmployee> {

    void anlysisEmpInfo(String comId);

}
