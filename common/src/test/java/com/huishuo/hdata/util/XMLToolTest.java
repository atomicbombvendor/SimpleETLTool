package com.huishuo.hdata.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLToolTest {

    @Test
    public void createXmlByMap() {

        String result = XMLTool.createXmlByMap(mockMap(), "Job");
        System.out.println(result);
    }

    @Test
    public void createXmlByMapContainsMap() {

        String result = XMLTool.createXmlByMap(mockMapContainsMap(), "Job");
        System.out.println(result);
    }

    @Test
    public void createXmlByMapList() {

        String result = XMLTool.createXmlByMap(mockListMap(), "Job");
        System.out.println(result);
    }

    @Test
    public void moveMapToXML() {

        Map<String, Object> mapAddKey = new LinkedHashMap<>();
        mapAddKey.put("Job", mockListMap());
        String result = XMLTool.moveMapToXML(mapAddKey);
        System.out.println(result);
    }

    @Test
    public void createMapByXml() {

        Map<String, Object> result = XMLTool.createMapByXml(getXML());
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void MapToXMLAndXMLToMap(){

        Map<String, Object> input = mockListMap();
        String xml = XMLTool.createXmlByMap(input, "Job");
        System.out.println(xml);
        Map<String, Object> map = XMLTool.createMapByXml(xml);
        System.out.println(JSON.toJSONString(map));
    }

    private static Map<String, Object> mockMap(){

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("JobName", "Job_Template");
        for (int i=0; i<10; i++){
            map.put("T"+i, "TR"+i);
        }
        return map;
    }

    private static Map<String, Object> mockMapContainsMap(){

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("JobName", "Job_Template");

        for (int i=0; i<10; i++){
            map.put("A"+i, "R"+i);
        }

        map.put("reader", mockMap());
        for (int i=0; i<10; i++){
            map.put("B"+i, "R"+i);
        }

        return map;
    }

    private static Map<String, Object> mockListMap(){

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("JobName", "Job_Template");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i=0; i<3; i++){
            list.add(mockMap());
        }

        map.put("Map3", list);
        return map;
    }

    private static String getXML(){

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<job>\n" +
                "\t<name>job_template</name>\n" +
                "    <reader>\n" +
                "\t\t<type>mysql</type>\n" +
                "\t\t<url>jdbc:mysql://127.0.0.1:3306/testdb</url>\n" +
                "\t\t<table>testtable</table>\n" +
                "\t\t<username>username</username>\n" +
                "\t\t<password>password</password>\n" +
                "\t\t<show_columns>A,B,C,D,E</show_columns>\n" +
                "\t\t<key_column>A</key_column>\n" +
                "\t</reader>\n" +
                "\t<reader>\n" +
                "\t\t<type>sqlserver</type>\n" +
                "\t\t<url>jdbc:sqlserver://127.0.0.1:3306/testdb</url>\n" +
                "\t\t<table>testtable</table>\n" +
                "\t\t<username>username</username>\n" +
                "\t\t<password>password</password>\n" +
                "\t\t<show_columns>A,F,G,K,H,I</show_columns>\n" +
                "\t\t<key_column>A</key_column>\n" +
                "\t</reader>\n" +
                "\t<reader>\n" +
                "\t\t<type>http_json</type>\n" +
                "\t\t<url>https://xxxxx.com/xxx/xxx</url>\n" +
                "\t\t<encoding>utf-8</encoding>\n" +
                "\t\t<paramter_format>A=%s&&B=%s</paramter_format>\n" +
                "\t\t<show_columns>A,B,C,D,E</show_columns>\n" +
                "\t\t<key_column>A</key_column>\n" +
                "\t</reader>\n" +
                "</job>";
    }
}