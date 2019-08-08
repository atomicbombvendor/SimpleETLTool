package com.huishuo.hdata.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileToolTest {

    private static String path = "C:\\tmp\\job_1.xml";

    @Test
    public void writeFile() {

        FileTool.writeFile("C:\\tmp", "t.xml", getXML());
    }

    @Test
    public void readFile() {

        String result = FileTool.readFile(path);
        System.out.println(result);
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