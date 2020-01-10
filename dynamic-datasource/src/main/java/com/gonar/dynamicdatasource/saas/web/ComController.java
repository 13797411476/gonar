package com.gonar.dynamicdatasource.saas.web;


import com.gonar.dynamicdatasource.saas.service.IComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 企业表 前端控制器
 * </p>
 *
 * @author YeJin
 * @since 2019-08-27
 */
@RestController
@RequestMapping("/saas/com")
public class ComController {

    @Autowired
    private IComService comService;

    @RequestMapping("selectById")
    public Object selectById(String comId) {
        return comService.selectById(comId);
    }

    @RequestMapping("selectOne")
    public Object selectOne(String comId) {
        return comService.getBaseMapper().selectById(comId);
    }

}

