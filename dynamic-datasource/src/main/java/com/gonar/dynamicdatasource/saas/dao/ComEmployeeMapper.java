package com.gonar.dynamicdatasource.saas.dao;

import com.gonar.dynamicdatasource.saas.entity.ComEmployee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gonar.dynamicdatasource.saas.entity.EmpInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author YeJin
 * @since 2019-08-28
 */
public interface ComEmployeeMapper extends BaseMapper<ComEmployee> {


    List<EmpInfo> selectLoginInfo(@Param("comId") String comId);

    List<EmpInfo> selectUnLoginInfo(@Param("comId") String comId);

    List<EmpInfo> selectConsumeInfo(@Param("comId") String comId, @Param("week") List<Integer> week);

}
