package com.huishuo.hdata.entity;

import java.util.Map;

public interface IDataSourceReaderFactroy {

    IDataSourceReader getDataSourceReader(Map<String, Object> parameterMap);


}
