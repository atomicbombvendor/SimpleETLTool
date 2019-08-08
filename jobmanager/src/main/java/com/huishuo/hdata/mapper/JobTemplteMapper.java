package com.huishuo.hdata.mapper;

import com.huishuo.hdata.entity.HDataJob;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobTemplteMapper {

    @Select("Select jobid, jobname, jobpath, jobdesc, jobstatus, createtime, updatedtime from hdatajobs where jobname=#{jobName}")
    public HDataJob getHDataJobByName(String jobName);

    @Delete("Delete from hdatajobs where jobname=#{jobName}")
    public int deleteHDataJobByName(String jobName);

    @Options(useGeneratedKeys = true, keyColumn = "jobId")
    @Insert("Insert into hdatajobs(jobname, jobpath, jobdesc, jobstatus, createtime, updatedtime) Values(#{jobName}, #{jobPath}, #{jobDesc}, #{jobStatus}, #{createTime}, #{updatedTime})")
    public int insertHDataJob(HDataJob jobTemplate);

    @Update("Update hdatajobs set jobname=#{jobName}, jobpath=#{jobPath}, jobdesc=#{jobDesc}, jobstatus=#{jobStatus}, updatedtime=CURRENT_TIMESTAMP where jobname=#{jobName}")
    public int updateHDataJob(HDataJob jobTemplate);

    @Select("Select jobid, jobname, jobpath, jobdesc, jobstatus, createtime, updatedtime from hdatajobs")
    public List<HDataJob> showAll();

}
