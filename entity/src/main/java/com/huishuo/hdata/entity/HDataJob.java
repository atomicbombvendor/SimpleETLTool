package com.huishuo.hdata.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class HDataJob implements Serializable {

    private int jobId;
    private String jobName;
    private String jobDesc;
    private int jobStatus;
    private String jobPath;
    private List<IDataSourceReader> dataSourceReaders;
    private LocalDateTime createTime;
    private LocalDateTime updatedTime;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public List<IDataSourceReader> getDataSourceReaders() {
        return dataSourceReaders;
    }

    public void setDataSourceReaders(List<IDataSourceReader> dataSourceReaders) {
        this.dataSourceReaders = dataSourceReaders;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getJobPath() {
        return jobPath;
    }

    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }

}
