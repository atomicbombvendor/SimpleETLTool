package com.huishuo.hdata.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DataReaderManagerServiceTest {

    @Autowired
    private DataReaderManagerService service;

    @Before
    public void init(){

    }

    @Test
    public void getData() {
        String result = service.getData("JobName_4", "N_3");
        System.out.println(result);
    }

    @Test
    public void test(){

        long l = 4334343343334343222L;
        float t = l;
        System.out.println(l);
        System.out.println(String.format("%.0f", t));

        String[][] strArr = new String[5][5];
        System.out.println(strArr[0][0]);
        System.out.println(strArr[0]);
    }
}