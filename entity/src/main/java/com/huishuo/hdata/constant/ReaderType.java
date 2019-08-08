package com.huishuo.hdata.constant;

import org.apache.commons.lang3.StringUtils;

public enum ReaderType {

    MYSQL("MySQL"),
    SQLSERVER("SQLServer"),
    ORACLE("Oracle"),
    HTTPJSON("Http_Json"),
    HTTPXML("Http_XML"),
    CSV("CSV")
    ;

    private String type;

    ReaderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ReaderType getReaderType(String type){
        if (StringUtils.isEmpty(type)){
            return null;
        }
        for (ReaderType enums : ReaderType.values()) {
            if (enums.getType().equalsIgnoreCase(type)) {
                return enums;
            }
        }
        return null;
    }
}
