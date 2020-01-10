package com.gonar.dynamicdatasource;

import com.gonar.dynamicdatasource.saas.service.IComEmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDatasourceApplicationTests {

    @Autowired
    private IComEmployeeService comEmployeeService;

    @Test
    public void contextLoads() {
        comEmployeeService.anlysisEmpInfo("1556439780143");
    }

}
