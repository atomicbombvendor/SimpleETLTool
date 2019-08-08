package com.huishuo.hdata.xml;

import com.huishuo.hdata.entity.HDataJob;
import com.huishuo.hdata.entity.IDataSourceReader;
import org.junit.Ignore;
import org.junit.Test;
import java.util.List;

@Ignore
public class HDataJobXMLTest {

    HDataJobXML hDataJobXml = new HDataJobXML("C:\\tmp");

    @Test
    public void parseXML() {

        String path = "C:\\tmp\\job_1.xml";
        HDataJob hDataJob = hDataJobXml.parseXML(path);
        List<IDataSourceReader> dsrs = hDataJob.getDataSourceReaders();
        System.out.println(dsrs.size());
    }

    @Test
    public void generateJobXML() {
        String path = "C:\\tmp\\job_1.xml";
        HDataJob hDataJob = hDataJobXml.parseXML(path);
        List<IDataSourceReader> dsrs = hDataJob.getDataSourceReaders();
        String path_2 = hDataJobXml.generateJobXML("Job_2", dsrs);
        System.out.println(path_2);
    }

    @Test
    public void updateJobXML() {

        String path = "C:\\tmp\\job_2.xml";
        HDataJob hDataJob = hDataJobXml.parseXML(path);
        List<IDataSourceReader> dsrs = hDataJob.getDataSourceReaders();
        String path_2 = hDataJobXml.updateJobXML(path, "job_3", dsrs);
        System.out.println(path_2);
    }

    @Test
    public void getXmlRootPath() {
        System.out.println(hDataJobXml.getXmlRootPath());
    }
}