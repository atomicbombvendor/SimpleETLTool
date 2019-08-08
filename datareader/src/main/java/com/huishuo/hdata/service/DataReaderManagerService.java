package com.huishuo.hdata.service;

import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.entity.HDataJob;
import com.huishuo.hdata.entity.IDataSourceReader;
import com.huishuo.hdata.entity.tuple.Tuple2;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataReaderManagerService {

    private static final Logger logger = LoggerFactory.getLogger(DataReaderManagerService.class);

    private final JobManagerService jobManagerService;

    public DataReaderManagerService(@Autowired JobManagerService jobManagerService) {
        this.jobManagerService = jobManagerService;
    }

    /**
     * 输入JobName和需要查询的关键Key值，得到JSON格式数据
     * @param jobName Job名，已经存在于数据库中
     * @param searchKey 一个Key只能查询出一条记录
     * @return
     */
    public String getData(String jobName, String searchKey){

        logger.info("get data. jobName={} searchKey={}", jobName, searchKey);

        HDataJob hDataJob = jobManagerService.getHDataJobByName(jobName);
        List<IDataSourceReader> dataSourceReaders = hDataJob.getDataSourceReaders();
        List<Tuple2<IDataSourceReader, Map<String, Object>>> metaDatas = new ArrayList<>(dataSourceReaders.size());
        for (IDataSourceReader reader : dataSourceReaders){
            Map<String, Object> metaData = getData(reader, searchKey);
            if (metaData == null || metaData.size() == 0){
                logger.error("read data is empty. reader={}", JSON.toJSONString(reader.getProperties()));
            }
            Tuple2 t = new Tuple2(reader, metaData);
            metaDatas.add(t);
        }

        return JSON.toJSONString(sumDatas(metaDatas));
    }


    private Map<String, Object> getData(IDataSourceReader dataSourceReader, String searchKey){

        return dataSourceReader.getData(searchKey);
    }

    /**
     * 组合不同数据源的数据，使用IDataSourceReader作为Key
     * @param metaDatas
     * @return
     */
    private Map<String, Object> sumDatas(List<Tuple2<IDataSourceReader, Map<String, Object>>> metaDatas){

        List<Tuple2<String, Map<String, Object>>> groupData = new ArrayList<>();
        for (Tuple2<IDataSourceReader, Map<String, Object>> meteData : metaDatas){

            Map<String, Object> properties = meteData._1().get().getProperties();
            Map<String, Object> data = meteData._2().get();
            String keyColumns = (String)properties.get("KeyColumns");
            String showColumns = (String)properties.get("ShowColumns");
            Set<String> columnsSet = getColumnsSet(keyColumns, showColumns);
            Tuple2<String, Map<String, Object>> temp =
                    new Tuple2<>(keyColumns, filterMap(columnsSet, data));
            groupData.add(temp);
        }

        return compositeData(groupData);
    }

    /**
     * 组合字符","分隔的字符串，得到Set
     * @param keyColumns
     * @param showColumns
     * @return
     */
    private Set<String> getColumnsSet(String keyColumns, String showColumns){

        Set<String> columnsSet = new HashSet<>();
        List<String> keyColumnList = StringUtils.split(keyColumns, ",", false);
        List<String> showColumnList = StringUtils.split(showColumns, ",", false);
        columnsSet.addAll(keyColumnList);
        columnsSet.addAll(showColumnList);
        return columnsSet;
    }

    /**
     * 倒序遍历Map，保证顺序；过滤数据中不需要显示的字段，得到所有需要显示的字段
     * @param filterKeys
     * @param sourceData
     * @return
     */
    private Map<String, Object> filterMap(final Set<String> filterKeys, Map<String, Object> sourceData){

        List<Map.Entry<String, Object>> resultList = sourceData.entrySet().stream().filter(map -> filterKeys.contains(map.getKey()))
                .collect(Collectors.toList());
        Map<String, Object> reverseListMap = resultList.stream().collect(Collectors.toMap(Map.Entry::getKey, m-> m.getValue(), (e1, e2)->e1, LinkedHashMap::new));
        return reverseListMap;
    }

    /**
     * 组合已经过滤过Key的数据
     * @param groupData
     * @return
     */
    private Map<String, Object> compositeData(List<Tuple2<String, Map<String, Object>>> groupData){

        Map<String, Object> data = new LinkedHashMap<>(groupData.get(0)._2().get());

        for (int i=1; i<groupData.size(); i++){
            Set<String> keyColumn = new HashSet<>(StringUtils.split(groupData.get(i)._1().get(), ",", false));
            //                                                         过滤掉主键字段
            groupData.get(i)._2().get().entrySet().stream().filter(m -> !keyColumn.contains(m.getKey()))
            .forEach(m -> data.put(m.getKey(), m.getValue()));
        }

        return data;
    }
}
