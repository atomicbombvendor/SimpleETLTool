package com.huishuo.hdata.service;

import com.huishuo.hdata.entity.HDataJob;
import com.huishuo.hdata.mapper.JobTemplteMapper;
import com.huishuo.hdata.xml.HDataJobXML;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobManagerService {

    private static final Logger logger = LoggerFactory.getLogger(JobManagerService.class);

    private JobTemplteMapper mapper;

    HDataJobXML hDataJobXml;

    public JobManagerService(@Autowired JobTemplteMapper jobTemplteMapper,
                             @Autowired HDataJobXML hDataJobXMl){
        this.mapper = jobTemplteMapper;
        this.hDataJobXml = hDataJobXMl;
    }

    /**
     * 根据JobName查询得到相关信息，并初始化DataSourceReader
     * @param jobName 存在的JobName，如果不存在，
     * @return 所有信息都被初始化的HDataJob
     */
    @Cacheable(cacheNames="hData", key = "#jobName")
    public HDataJob getHDataJobByName(String jobName){

        HDataJob targetJob = mapper.getHDataJobByName(jobName);
        if (targetJob == null){
            logger.error("not exits job info. jobName={}", jobName);
            return null;
        }

        HDataJob sourceJob = hDataJobXml.parseXML(targetJob.getJobPath());

        sourceJob.setJobId(targetJob.getJobId());
        sourceJob.setJobDesc(targetJob.getJobDesc());
        sourceJob.setJobStatus(targetJob.getJobStatus());
        sourceJob.setCreateTime(targetJob.getCreateTime());
        sourceJob.setUpdatedTime(targetJob.getUpdatedTime());

        return sourceJob;
    }

    /**
     * 根据JobName删除数据库中的记录并且删除XML文件
     * @param jobName 存在的JobName
     * @return 删除的记录数
     */
    @CacheEvict(cacheNames="hData", key = "#jobName")
    public int deleteHDataJobByName(String jobName){
        HDataJob targetJob = mapper.getHDataJobByName(jobName);
        if (targetJob == null){
            logger.error("not exits job info. jobName={}", jobName);
            return 0;
        }
        int result = mapper.deleteHDataJobByName(jobName);
        if (result != 0){
            hDataJobXml.deleteJobXML(targetJob.getJobPath());
        }
        return result;
    }

    /**
     * 根据DataSourceReader生成XML文件
     * @param hDataJob DataSourceReader已经被初始化的对象
     * @return 文件路径
     */
    public int insertHDataJob(HDataJob hDataJob){
        if (hDataJob.getDataSourceReaders() == null) {
            logger.error("there is not DataSourceReaders. JobName={}", hDataJob.getJobName());
            return 0;
        }
        String jobPath = hDataJobXml.generateJobXML(hDataJob.getJobName(), hDataJob.getDataSourceReaders());
        if (StringUtils.isEmpty(jobPath)){
            return 0;
        }
        hDataJob.setJobPath(jobPath);
        return mapper.insertHDataJob(hDataJob);
    }

    /**
     * 更新XML文件
     * @param hDataJob DataSourceReader已经被初始化的对象
     * @return 更新的记录数
     */
    @CachePut(cacheNames="hData", key = "#hDataJob.jobName")
    public int updateHDataJobXMLContent(HDataJob hDataJob){

        if (hDataJob.getDataSourceReaders() == null) {
            logger.error("there is not DataSourceReaders. JobName={}", hDataJob.getJobName());
            return 0;
        }
        String jobPath = hDataJobXml.updateJobXML(hDataJob.getJobPath(), hDataJob.getJobName(), hDataJob.getDataSourceReaders());
        if (Strings.isEmpty(jobPath)){
            return 0;
        }
        hDataJob.setJobPath(jobPath);
        return mapper.updateHDataJob(hDataJob);
    }

    /**
     * 展示所有的记录
     * @return DataSourceReaders已经被初始化后的对象
     */
    public List<HDataJob> showAll(){
        List<HDataJob> hDataJobs = mapper.showAll();
        for (HDataJob job : hDataJobs){

            HDataJob temp = hDataJobXml.parseXML(job.getJobPath());
            job.setDataSourceReaders(temp.getDataSourceReaders());
        }
        return hDataJobs;
    }
}
