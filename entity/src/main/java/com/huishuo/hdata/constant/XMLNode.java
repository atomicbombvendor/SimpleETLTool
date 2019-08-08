package com.huishuo.hdata.constant;

public enum XMLNode {
    JOBNAME("JobName"),
    JOB("Job"),
    READER("Reader"),
    TYPE("Type");

    private String nodeName;

    XMLNode(String nodeName){
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
