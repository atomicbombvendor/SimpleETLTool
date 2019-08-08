package com.huishuo.hdata.entity.sourcereader;

import com.alibaba.fastjson.JSONObject;
import com.huishuo.hdata.constant.ReaderType;
import com.huishuo.hdata.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTTPJSONDataSourceReader extends DataSourceReader {

    private String type;
    private String url;
    private String userName;
    private String password;
    private String encoding;
    private String parametersFormat;
    private List<String> showColumns;
    private List<String> keyColumns;

    @SuppressWarnings("Unchecked")
    public HTTPJSONDataSourceReader(Map parameters) {
        super(parameters);
        this.type = parameters.containsKey("Type") ? parameters.get("Type").toString() : null;
        this.url = parameters.containsKey("URL") ? parameters.get("URL").toString() : null;
        this.userName = parameters.containsKey("UserName") ? parameters.get("UserName").toString(): null;
        this.password = parameters.containsKey("Password") ? parameters.get("Password").toString(): null;
        this.encoding = parameters.containsKey("Encoding") ? parameters.get("Encoding").toString(): null;
        this.parametersFormat = parameters.containsKey("ParametersFormat") ? parameters.get("ParametersFormat").toString() : null;
        this.showColumns = parameters.containsKey("ShowColumns") ? StringUtil.splitString(parameters.get("ShowColumns").toString()) : null;
        this.keyColumns = parameters.containsKey("KeyColumns") ? StringUtil.splitStringObject(parameters.get("KeyColumns")) : null;
    }

    @Override
    public Map<String, Object> getData(Object parameter) {

        String json = null;
        return getMap(json);
    }

    @Override
    public String getType() {
        return ReaderType.HTTPJSON.getType();
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("Type", type);
        properties.put("ConnectionURL", url);
        properties.put("UserName", userName);
        properties.put("Password", password);
        properties.put("Encoding", encoding);
        properties.put("ParametersFormat", parametersFormat.replaceAll("&amp;", "&"));
        properties.put("ShowColumns",  StringUtils.join(showColumns, ","));
        properties.put("KeyColumns",  StringUtils.join(keyColumns, ","));
        return properties;
    }

    private Map<String, Object> getMap(String json){
        if (json.isEmpty()){
            return null;
        }
        return JSONObject.parseObject(json ,Map.class);
    }

}
