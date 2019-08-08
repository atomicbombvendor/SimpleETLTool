package com.huishuo.hdata.entity;

import java.io.Serializable;
import java.util.Map;

public interface IDataSourceReader extends Serializable {

    /**
     * 从数据源返回的数据
     * @param parameter
     * @return
     */
    Map<String, Object> getData(Object parameter);

    /**
     * 当前的类型，比如MySQL, SQLServer, HHTP
     * @return
     */
    String getType();

    /**
     * 得到所有的属性，必要的三个字段：Type, KeyColumns，ShowColumns
     * @return
     */
    Map<String, Object> getProperties();
}
