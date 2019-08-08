package com.huishuo.hdata.service;


import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.entity.HDataJob;
import com.huishuo.hdata.xml.HDataJobXML;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JobManagerServiceTest {

    @Autowired
    private JobManagerService jobManagerService;

    @Autowired
    private HDataJobXML hDataJobXML;

    @Test
    public void getHDataJobByName() {

        String jobName = "JobName_54";
        HDataJob result = jobManagerService.getHDataJobByName(jobName);
        Assert.assertEquals(3, result.getDataSourceReaders().size());
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void deleteHDataJobByName() {

        String jobName = "JobName_55";
        int result = jobManagerService.deleteHDataJobByName(jobName);
        Assert.assertEquals(1, result);
    }

//    @Test
//    public void insertHDataJob() {
//
//        int result = jobManagerService.insertHDataJob(mockHDataJob());
//        Assert.assertEquals(1, result);
//    }

    @Test
    public void insertHDataJob(){

        int result = jobManagerService.insertHDataJob(mockHDataJob2());
        Assert.assertEquals(1, result);
    }

    @Test
    public void updateHDataJobXMLContent() {

        HDataJob mock = mockHDataJob();
        mock.setJobName("JobName_55");
        mock.setJobPath("C:\\tmp\\JobName_55.xml");
        int result = jobManagerService.updateHDataJobXMLContent(mock);
        Assert.assertEquals(1, result);
    }

    @Test
    public void showAll() {

        List<HDataJob> result = jobManagerService.showAll();
        System.out.println(JSON.toJSONString(result));
    }

    private HDataJob mockHDataJob(){

        String jobName = "JobName_" + RandomUtils.nextInt(1, 100);
        HDataJob templte = new HDataJob();
        templte.setJobName(jobName);
        templte.setJobDesc("Mock Job");
        templte.setJobPath("C:\\tmp\\" + jobName + ".xml");
        templte.setJobStatus(1);
        templte.setCreateTime(LocalDateTime.now());
        templte.setUpdatedTime(LocalDateTime.now());

        templte.setDataSourceReaders(hDataJobXML.parseXML("C:\\tmp\\job_3.xml").getDataSourceReaders());

        return templte;
    }

    private HDataJob mockHDataJob2(){

        String jobName = "JobName_4";
        HDataJob templte = new HDataJob();
        templte.setJobName(jobName);
        templte.setJobDesc("Mock Job");
        templte.setJobPath("C:\\tmp\\job_4.xml");
        templte.setJobStatus(1);
        templte.setCreateTime(LocalDateTime.now());
        templte.setUpdatedTime(LocalDateTime.now());

        templte.setDataSourceReaders(hDataJobXML.parseXML("C:\\tmp\\job_4.xml").getDataSourceReaders());

        return templte;
    }
}