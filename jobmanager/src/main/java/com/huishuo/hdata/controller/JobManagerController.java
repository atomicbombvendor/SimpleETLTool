package com.huishuo.hdata.controller;

import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.entity.HDataJob;
import com.huishuo.hdata.service.JobManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("JobManager")
public class JobManagerController {

    private static final Logger logger = LoggerFactory.getLogger(JobManagerController.class);

    @Autowired
    private JobManagerService service;

    @GetMapping(path = {"/getJob/{jobName}"})
    public String getHDataJobByName(@PathVariable String jobName){

        HDataJob result = service.getHDataJobByName(jobName);
        return JSON.toJSONString(result);
    }

    @DeleteMapping(path = {"/deleteJob/{jobName}"})
    public boolean deleteHDataJobByName(String jobName){

        int result = service.deleteHDataJobByName(jobName);
        if (result > 0){
            return true;
        }
        return false;
    }
}
