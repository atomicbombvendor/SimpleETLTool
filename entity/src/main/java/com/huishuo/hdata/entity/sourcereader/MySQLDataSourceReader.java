package com.huishuo.hdata.entity.sourcereader;

import com.huishuo.hdata.constant.ReaderType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;

public class MySQLDataSourceReader extends JDBCDataSourceReader{

    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * XML节点应该使用标准的驼峰表达式；对于代码中的变量使用首字母小写的驼峰表达式。
     *
     * @param parameters XML节点的参数
     */
    public MySQLDataSourceReader(Map parameters) {
        super(parameters);
    }

    @Override
    public void setDriveClassName(DriverManagerDataSource dataSource) {
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
    }

    @Override
    String getSQL(Object searchValue){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(StringUtils.join(this.getKeyColumns(), ","));
        sb.append(StringUtils.join(this.getShowColumns(), ","));
        sb.append(" FROM ").append(this.getTableName()).append(" WHERE ")
                .append(this.getKeyColumns()).append(" = '").append(searchValue.toString())
                .append("'");

        return sb.toString();
    }

    @Override
    public String getType() {
        return ReaderType.MYSQL.getType();
    }

}
