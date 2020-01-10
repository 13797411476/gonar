package com.gonar.dynamicdatasource.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author YeJin
 * @date 2019/8/27 16:56
 */
@Component
@Aspect
@Slf4j
public class DataSourceSwitchAspect {

    @Pointcut("execution(* com.gonar.dynamicdatasource.saas.dao..*.*(..))")
    public void saasSourcePointCut() {
    }
    @Pointcut("execution(* com.gonar.dynamicdatasource.crm.dao..*.*(..))")
    public void crmSourcePointCut() {
    }
    @Pointcut("execution(* com.gonar.dynamicdatasource.shop.dao..*.*(..))")
    public void shopSourcePointCut() {
    }

    @Around("saasSourcePointCut()")
    public Object aroundSaasSourcePointCut(ProceedingJoinPoint point) throws Throwable {
        return handle(point, DBTypeEnum.saasDataSource);
    }
    @Around("crmSourcePointCut()")
    public Object aroundCrmSourcePointCut(ProceedingJoinPoint point) throws Throwable {
        return handle(point, DBTypeEnum.crmDataSource);
    }
    @Around("shopSourcePointCut()")
    public Object aroundShopSourcePointCut(ProceedingJoinPoint point) throws Throwable {
        return handle(point, DBTypeEnum.shopDataSource);
    }

    private Object handle(ProceedingJoinPoint point, DBTypeEnum dbTypeEnum) throws Throwable {
        try {
            if(!dbTypeEnum.getValue().equals(DynamicDataSource.getDataSource())) {
                log.info("set datasource is " + dbTypeEnum.getValue());
                DynamicDataSource.setDataSource(dbTypeEnum);
            }
            return point.proceed();
        } finally {
            log.debug("clean datasource");
            DynamicDataSource.clearDataSource();
        }
    }

}