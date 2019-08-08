package com.huishuo.hdata.entity.sourcereader;

import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.constant.ReaderType;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


public class MySQLDataSourceReaderTest {

    private MySQLDataSourceReader reader;

    @Before
    public void init(){

        reader = new MySQLDataSourceReader(mockParameters());
    }


    @Test
    public void getData() {

        Map<String, Object> result = reader.getData("N_2");
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void setDriveClassName() {
    }

    @Test
    public void getSQL() {
        String sql = reader.getSQL("N_2");
        System.out.println(sql);
    }

    private Map<String, Object> mockParameters(){
        Map<String, Object> map = new HashMap<>();
        map.put("Type", ReaderType.MYSQL.getType());
        map.put("URL", "jdbc:mysql://localhost:3306/hdata?serverTimezone=GMT%2B8");
        map.put("UserName", "test");
        map.put("Password", "666666");
        map.put("TableName", "data_1");
        List<String> t_sc = Arrays.asList(StringUtils.split("CA,CB,CC,CD,CE,CF,CG", ","));
        map.put("ShowColumns", StringUtils.join(t_sc, ","));
        map.put("KeyColumns", StringUtils.join(Arrays.asList("Name"), ","));

        return map;
    }
}