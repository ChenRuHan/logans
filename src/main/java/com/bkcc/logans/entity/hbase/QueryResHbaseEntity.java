package com.bkcc.logans.entity.hbase;

import com.bkcc.hbase.annotations.HBaseColumn;
import com.bkcc.hbase.annotations.HBaseRowkey;
import com.bkcc.hbase.annotations.HBaseTable;
import lombok.Data;

import java.io.Serializable;

/**
 * 【描 述】：查询结果实体类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-23 16:13
 */
@Data
@HBaseTable(tableName = "query_res", nameSpace = "logans")
public class QueryResHbaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 【描 述】：(当前任务单号反转)-(参与分析的字段加密之后值)-(6位索引值)
     *
     *  @since  Jul 17, 2019 v1.0
     */
    @HBaseRowkey
    private String rowKey;

    /**
     * 【描 述】：占位用，暂时保留
     *
     *  @since  Aug 6, 2019 v1.0
     */
    @HBaseColumn
    private String r;

}///:~
