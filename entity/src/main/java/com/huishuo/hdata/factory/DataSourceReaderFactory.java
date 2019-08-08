package com.huishuo.hdata.factory;

import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.constant.ReaderType;
import com.huishuo.hdata.constant.XMLNode;
import com.huishuo.hdata.entity.*;
import com.huishuo.hdata.entity.sourcereader.HTTPJSONDataSourceReader;
import com.huishuo.hdata.entity.sourcereader.MySQLDataSourceReader;
import com.huishuo.hdata.entity.sourcereader.SQLServerDataSourceReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DataSourceReaderFactory implements IDataSourceReaderFactroy {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceReaderFactory.class);

    @Override
    public IDataSourceReader getDataSourceReader(Map<String, Object> parameterMap) {

        if (!parameterMap.containsKey(XMLNode.TYPE.getNodeName()) || StringUtils.isEmpty(parameterMap.get(XMLNode.TYPE.getNodeName()).toString())){
            logger.error("Parameters is not contains ${type}, parameters={}", JSON.toJSONString(parameterMap));
        }

        ReaderType type = ReaderType.getReaderType(parameterMap.get(XMLNode.TYPE.getNodeName()).toString());

        IDataSourceReader dataSourceReader;

        switch (type){
            case MYSQL:
                dataSourceReader = new MySQLDataSourceReader(parameterMap);
                break;
            case SQLSERVER:
                dataSourceReader = new SQLServerDataSourceReader(parameterMap);
                break;
            case HTTPJSON:
                dataSourceReader = new HTTPJSONDataSourceReader(parameterMap);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return dataSourceReader;
    }
}
