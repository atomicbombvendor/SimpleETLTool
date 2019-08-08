package com.huishuo.hdata.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * XML和Map互相转换工具
 */
public class XMLTool {

    // 博客源码：https://blog.csdn.net/woshishui891014/article/details/82022018

    /**
     * 将Map转换为XML,Map可以多层转
     * @param parentName 就是map的根key,如果map没有根key,就输入转换后的xml根节点。
     * @return String-->XML content
     */
    @SuppressWarnings("unchecked")
    public static String createXmlByMap(Map<String, Object> map,
                                        String parentName) {
        // 获取map的key对应的value
        Map<String, Object> rootMap= (Map<String, Object>)map.get(parentName);
        if (rootMap==null) {
            rootMap=map;
        }
        Document doc = DocumentHelper.createDocument();
        // 设置根节点
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(), parentName, rootMap);
        return formatXML(xml);
    }

    /**
     * 循环遍历params创建xml节点
     * @param element 根节点
     * @param parentName 子节点名字
     * @param params map数据
     * @return String-->Xml
     */
    @SuppressWarnings("unchecked")
    private static String iteratorXml(Element element, String parentName,
                                      Map<String, Object> params) {
        Element e = element.addElement(parentName);
        Set<String> set = params.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String key = it.next();
            if (params.get(key) instanceof Map) {
                iteratorXml(e, key, (Map<String, Object>) params.get(key));
            } else if (params.get(key) instanceof List) {
                List<Object> list = (List<Object>) params.get(key);
                for (int i = 0; i < list.size(); i++) {
                    iteratorXml(e, key, (Map<String, Object>) list.get(i));
                }
            } else {
                String value = params.get(key) == null ? "" : params.get(key)
                        .toString();
                e.addElement(key).addText(value);
                // e.addElement(key).addCDATA(value);
            }
        }
        return e.asXML();
    }

    /**
     * 格式化xml
     * @param xml
     * @return
     */
    private static String formatXML(String xml) {
        String requestXML = null;
        XMLWriter writer = null;
        Document document;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new StringReader(xml));
            if (document != null) {
                StringWriter stringWriter = new StringWriter();
                // 格式化，每一级前的空格
                OutputFormat format = new OutputFormat(" ", true);
                // xml声明与内容是否添加空行
                format.setNewLineAfterDeclaration(false);
                // 是否设置xml声明头部 false：添加
                format.setSuppressDeclaration(false);
                // 设置分行
                format.setNewlines(true);
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                requestXML = stringWriter.getBuffer().toString();
            }
            return requestXML;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * 第二种
     * 手动组装map转为string;使用字符串拼接的方式
     * @param map
     * @return
     */
    public static String moveMapToXML(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        mapToXML(map, sb);
        return formatXML(sb.toString());
    }

    /**
     *
     * @param map 循环遍历map节点和value
     * @param sb 输出xml
     */
    private static void mapToXML(Map<?, ?> map, StringBuffer sb) {
        Set<?> set = map.keySet();
        for (Iterator<?> it = set.iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (value instanceof HashMap) {
                sb.append("<").append(key).append(">");
                mapToXML((HashMap<?, ?>) value, sb);
                sb.append("</").append(key).append(">");
            } else if (value instanceof ArrayList) {
                List<?> list = (ArrayList<?>) map.get(key);
                for (Object o : list) {
                    sb.append("<").append(key).append(">");
                    Map<?, ?> hm = (HashMap<?, ?>) o;
                    mapToXML(hm, sb);
                    sb.append("</").append(key).append(">");
                }
            } else {
                sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
            }

        }
    }

    /**
     * 通过XML转换为Map<String,Object>
     * @param xml 为String类型的Xml
     * @return 第一个为Root节点，Root节点之后为Root的元素，如果为多层，可以通过key获取下一层Map
     */
    public static Map<String, Object> createMapByXml(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(filterIllegal(xml));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        if (doc == null) {
            return map;
        }
        Element rootElement = doc.getRootElement();
        elementToMap(rootElement, map);
        return map;
    }

    /***
     * XmlToMap核心方法，里面有递归调用
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> elementToMap(Element outele,
                                                    Map<String, Object> outmap) {
        List<Element> list = outele.elements();
        int size = list.size();
        if (size == 0) {
            outmap.put(outele.getName(), outele.getTextTrim());
        } else {
            Map<String, Object> childNodeMap = new HashMap<>();
            for (int i=0; i< list.size(); i++) {
                Element eleTmp = list.get(i);
                String eleName = eleTmp.getName();
                // 从innermap中得到相同的Key
                Object obj = childNodeMap.get(eleName);
                if (obj == null) {
                    elementToMap(eleTmp, childNodeMap);
                } else {//找到相同的Key
                    if (obj instanceof Map) {
                        List<Map<String, Object>> sameNameNodes = new ArrayList<>();
                        sameNameNodes.add((Map<String, Object>) childNodeMap
                                .remove(eleName));
                        elementToMap(eleTmp, childNodeMap);
                        sameNameNodes.add((Map<String, Object>) childNodeMap
                                .remove(eleName));
                        childNodeMap.put(eleName, sameNameNodes);
                    } else {
                        Map<String, Object> emptyChildNodeMap = new HashMap<>();
                        elementToMap(eleTmp, emptyChildNodeMap);
                        ((List<Map<String, Object>>) obj).add((Map<String, Object>)emptyChildNodeMap.get(eleName));
                    }
                }
            }
            outmap.put(outele.getName(), childNodeMap);
        }
        return outmap;
    }

    public static String filterIllegal(String xmlContent){
        String illegalAnd = "&";
        String legalAnd = "&amp;";
        return xmlContent.replaceAll(illegalAnd, legalAnd);
    }

    public static String restoreIllegal(String xmlContent){
        String illegalAnd = "&";
        String legalAnd = "&amp;";
        return xmlContent.replaceAll(legalAnd, illegalAnd);
    }
}
