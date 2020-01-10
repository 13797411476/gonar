package com.gonar.dynamicdatasource.saas.service.impl;

import com.gonar.dynamicdatasource.saas.entity.Com;
import com.gonar.dynamicdatasource.saas.dao.ComMapper;
import com.gonar.dynamicdatasource.saas.service.IComService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 企业表 服务实现类
 * </p>
 *
 * @author YeJin
 * @since 2019-08-27
 */
@Service
public class ComServiceImpl extends ServiceImpl<ComMapper, Com> implements IComService {

    @Resource
    private ComMapper comMapper;

    @Override
    public Object selectById(String comId) {
        return comMapper.selectById(comId);
    }
}
