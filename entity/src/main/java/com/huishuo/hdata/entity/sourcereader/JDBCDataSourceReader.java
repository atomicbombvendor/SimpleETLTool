package com.huishuo.hdata.entity.sourcereader;

import com.alibaba.fastjson.JSON;
import com.huishuo.hdata.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class JDBCDataSourceReader extends DataSourceReader{

    private static final Logger logger = LoggerFactory.getLogger(JDBCDataSourceReader.class);

    private String type;
    private String url;
    private String userName;
    private String password;
    private String tableName;
    private List<String> showColumns;
    private String keyColumns;

    private JdbcTemplate jdbcTemplate;

    /**
     * XML节点应该使用标准的驼峰表达式；对于代码中的变量使用首字母小写的驼峰表达式。
     * @param parameters XML节点的参数
     */
    public JDBCDataSourceReader(Map<String, Object> parameters){
        super(parameters);

        this.type = parameters.containsKey("Type") ? parameters.get("Type").toString() : null;
        this.url = parameters.containsKey("URL") ? parameters.get("URL").toString() : null;
        this.userName = parameters.containsKey("UserName") ? parameters.get("UserName").toString() : null;
        this.password = parameters.containsKey("Password") ? parameters.get("Password").toString() : null;
        this.tableName = parameters.containsKey("TableName") ? parameters.get("TableName").toString() : null;
        this.showColumns =
                parameters.containsKey("ShowColumns") ? StringUtil.splitString(parameters.get("ShowColumns").toString()) : null;
        this.keyColumns =
                parameters.containsKey("KeyColumns") ? parameters.get("KeyColumns").toString() : null;
    }

    @Override
    public Map<String, Object> getData(Object parameter) {
        try {
            connection();
            String sql = getSQL(parameter);
            logger.info("reader sql. sql={}", sql);
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            if (result.size() == 0){
                throw new EmptyResultDataAccessException(1);
            }else if (result.size() > 1){
                logger.error("read data more than 1, return first. data={}", JSON.toJSONString(result));
                return result.get(0);
            }else{
                return result.get(0);
            }
        } catch (DataAccessException dae) {
            logger.error("read data is error. exception={}", ExceptionUtils.getStackTrace(dae));
            return null;
        }
    }

    /**
     * 进行数据库连接部分
     */
    public void connection() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        setDriveClassName(dataSource);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public abstract void setDriveClassName(DriverManagerDataSource dataSource);

    /**
     * 默认一次查询一个关键字
     * @param searchValue
     * @return
     */
    abstract String getSQL(Object searchValue);

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Map<String, Object> getProperties(){

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("Type", type);
        properties.put("URL", url);
        properties.put("UserName", userName);
        properties.put("Password", password);
        properties.put("TableName", tableName);
        properties.put("ShowColumns", StringUtils.join(showColumns, ","));
        properties.put("KeyColumns", keyColumns);

        return properties;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getShowColumns() {
        return showColumns;
    }

    public String getKeyColumns() {
        return keyColumns;
    }
}
