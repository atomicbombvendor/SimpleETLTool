package com.huishuo.hdata.reader;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public interface IReader {

    /**
     * 获取数据准备方法
     * @param jonName 根据JobName获取准备数据
     */
    void prepare(String jonName);

    /**
     * 查询数据
     * @param searchValue 需要查询的值
     * @return JSON数据
     */
    JSON getData(String searchValue);
}
