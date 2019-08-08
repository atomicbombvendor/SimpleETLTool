package com.huishuo.hdata.xml;

import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.constant.XMLNode;
import com.huishuo.hdata.entity.IDataSourceReader;
import com.huishuo.hdata.entity.HDataJob;
import com.huishuo.hdata.factory.DataSourceReaderFactory;
import com.huishuo.hdata.util.FileTool;
import com.huishuo.hdata.util.StringUtil;
import com.huishuo.hdata.util.XMLTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.JobName;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class HDataJobXML {

    private static final Logger logger = LoggerFactory.getLogger(HDataJobXML.class);

    @Value("${xml.root.path}")
    private String xmlRootPath;

    public HDataJobXML(@Value("${xml.root.path}") String xmlRootPath){
        this.xmlRootPath = xmlRootPath;
    }

    /**
     * 解析XML文件, 得到初始化DataSourceReader，JobName，JobPath属性的对象实例
     * @param xmlPath XML文件绝对路径
     * @return Job模板
     */
    public HDataJob parseXML(String xmlPath){
        if (!Paths.get(xmlPath).toFile().isFile() || !Paths.get(xmlPath).toFile().exists()){
            logger.error("File is not exists or not file. path={}", xmlPath);
            return null;
        }
        String xmlContent = FileTool.readFile(xmlPath);
        Map<String, Object> xmlMap = XMLTool.createMapByXml(xmlContent);
        if (!xmlMap.containsKey(XMLNode.JOB.getNodeName())){
            logger.error(String.format("Read xml error. xmlPath=%s", xmlPath));
            return null;
        }

        HDataJob hDataJob = new HDataJob();
        parseHDataJob(xmlMap, hDataJob);
        hDataJob.setJobPath(xmlPath);
        return hDataJob;
    }

    /**
     * 使用配置文件中配置的Root路径，生成JobXML
     * @param dsrs dataSourceReader
     * @return XML绝对路径
     */
    public String generateJobXML(String jobName, List<IDataSourceReader> dsrs){

        String fileName = jobName + ".xml";
        Map<String, Object> xmlMap = new LinkedHashMap<>();
        xmlMap.put(XMLNode.JOBNAME.getNodeName(), jobName);

        List<Map<String, Object>> readers = new ArrayList<>();
        for (IDataSourceReader drs : dsrs) {
            readers.add(drs.getProperties());
        }
        xmlMap.put(XMLNode.READER.getNodeName(), readers);

        String xmlContent = XMLTool.createXmlByMap(xmlMap, XMLNode.JOB.getNodeName());
        if (FileTool.writeFile(xmlRootPath, fileName, xmlContent)) {
            return Paths.get(xmlRootPath, fileName).toString();
        }else{
            logger.error("generate xml fail. xml={}", fileName);
            return StringUtils.EMPTY;
        }
    }

    /**
     * 更新JobXML的值;删除然后新建
     * @param xmlPath XML文件绝对路径
     * @param jobName Job名
     * @param dsrs DataSourceReaders
     * @return 更新后XML文件绝对路径
     */
    public String updateJobXML(String xmlPath, String jobName, List<IDataSourceReader> dsrs){

        if (!FileTool.fileExists(xmlPath)){
            logger.error("XML is not file or exists. xmlPath={}", xmlPath);
            return StringUtils.EMPTY;
        }
        FileTool.deleteFile(xmlPath);
        return generateJobXML(jobName, dsrs);
    }

    /**
     * 删除XML文件
     * @param xmlPath
     */
    public void deleteJobXML(String xmlPath){
        FileTool.deleteFile(xmlPath);
    }

    public String getXmlRootPath(){
        return xmlRootPath;
    }

    /**
     * 填充HDataJob中的各个属性值
     * @param xmlMap 从XML中转换得到Map
     * @param hDataJob 需要填充的HDataJob对象
     */
    private void parseHDataJob(Map<String, Object> xmlMap, HDataJob hDataJob){

        if (!xmlMap.containsKey(XMLNode.JOB.getNodeName())){
            logger.error("XML don't contains Job Node. XML_JSON={}", JSON.toJSONString(xmlMap));
        }

        Map<String, Object> job = (Map<String, Object>)xmlMap.get(XMLNode.JOB.getNodeName());
        hDataJob.setJobName(job.get(XMLNode.JOBNAME.getNodeName()).toString());
        List<Map<String, Object>> readers = (List<Map<String, Object>>)job.get(XMLNode.READER.getNodeName());
        List<IDataSourceReader> dsrs = new ArrayList<>();
        DataSourceReaderFactory factory = new DataSourceReaderFactory();
        for (Map<String, Object> reader : readers){

            IDataSourceReader dsr = factory.getDataSourceReader(reader);
            dsrs.add(dsr);
        }
        hDataJob.setDataSourceReaders(dsrs);
    }

}


