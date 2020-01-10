package com.gonar.dynamicdatasource.saas.service.impl;

import com.gonar.dynamicdatasource.saas.entity.ComEmployee;
import com.gonar.dynamicdatasource.saas.dao.ComEmployeeMapper;
import com.gonar.dynamicdatasource.saas.entity.EmpInfo;
import com.gonar.dynamicdatasource.saas.service.IComEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YeJin
 * @since 2019-08-28
 */
@Service
public class ComEmployeeServiceImpl extends ServiceImpl<ComEmployeeMapper, ComEmployee> implements IComEmployeeService {

    @Autowired
    private ComEmployeeMapper comEmployeeMapper;

    @Override
    public void anlysisEmpInfo(String comId) {
        List<EmpInfo> empInfos = comEmployeeMapper.selectLoginInfo(comId);


        List<Integer> week = new ArrayList<>();
        List<Integer> oneMonth = new ArrayList<>();
        List<Integer> threeMonth = new ArrayList<>();
        List<Integer> halfYear = new ArrayList<>();
        List<Integer> total = new ArrayList<>();
        for (EmpInfo empInfo : empInfos) {
            Date loginTime = empInfo.getLoginTime();
            if (compare(loginTime, Calendar.DATE, -7)) {
                if (!week.contains(empInfo.getUserId())) {
                    week.add(empInfo.getUserId());
                }
            }
            if (compare(loginTime, Calendar.MONTH, -1)) {
                if (!oneMonth.contains(empInfo.getUserId())) {
                    oneMonth.add(empInfo.getUserId());
                }
            }
            if (compare(loginTime, Calendar.MONTH, -3)) {
                if (!threeMonth.contains(empInfo.getUserId())) {
                    threeMonth.add(empInfo.getUserId());
                }
            }
            if (compare(loginTime, Calendar.MONTH, -6)) {
                if (!halfYear.contains(empInfo.getUserId())) {
                    halfYear.add(empInfo.getUserId());
                }
            }
            if (!total.contains(empInfo.getUserId())) {
                total.add(empInfo.getUserId());
            }
        }
        System.out.print(week.size() + "   ");
        System.out.print(oneMonth.size() + "   ");
        System.out.print(threeMonth.size() + "   ");
        System.out.print(halfYear.size() + "   ");
        System.out.print(total.size() + "   ");
        System.out.println();


        List<EmpInfo> empInfo2 = comEmployeeMapper.selectUnLoginInfo(comId);
        System.out.println(empInfo2.size());

        List<EmpInfo> week1 = comEmployeeMapper.selectConsumeInfo(comId, week);
        List<Integer> week2 = getIntegers(week1, Calendar.DAY_OF_MONTH, -7);

        List<EmpInfo> oneMonth1 = comEmployeeMapper.selectConsumeInfo(comId, oneMonth);
        List<Integer> oneMonth2 = getIntegers(oneMonth1, Calendar.MONTH, -1);

        List<EmpInfo> threeMonth1 = comEmployeeMapper.selectConsumeInfo(comId, threeMonth);
        List<Integer> threeMonth2 = getIntegers(threeMonth1, Calendar.MONTH, -3);

        List<EmpInfo> halfYear1 = comEmployeeMapper.selectConsumeInfo(comId, halfYear);
        List<Integer> halfYear2 = getIntegers(halfYear1, Calendar.MONTH, -6);

        List<EmpInfo> total1 = comEmployeeMapper.selectConsumeInfo(comId, total);
        List<Integer> total2 = getIntegers(total1, -100, 0);

        System.out.print((week.size()-week2.size()) + "   ");
        System.out.print((oneMonth.size()-oneMonth2.size()) + "   ");
        System.out.print((threeMonth.size()-threeMonth2.size()) + "   ");
        System.out.print((halfYear.size()-halfYear2.size()) + "   ");
        System.out.print((total.size()-total2.size()) + "   ");

        System.out.println();

    }

    private List<Integer> getIntegers(List<EmpInfo> week1, int i, int j) {
        List<Integer> week2 = new ArrayList<>();
        for (EmpInfo empInfo : week1) {
            Date loginTime = empInfo.getLoginTime();
            if(i == -100) {
                if (!week2.contains(empInfo.getUserId())) {
                    week2.add(empInfo.getUserId());
                }
            }else if(compare(loginTime, i, j)) {
                if (!week2.contains(empInfo.getUserId())) {
                    week2.add(empInfo.getUserId());
                }
            }
        }
        return week2;
    }

    private void print(List<EmpInfo> empInfos) {

    }

    private boolean compare(Date loginTime, int pattern, int time) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(pattern, time);
        long timeInMillis = calendar.getTimeInMillis();
        long loginTimeMillis = loginTime.getTime();
        if (loginTimeMillis > timeInMillis) {
            return true;
        }
        return false;
    }
}
