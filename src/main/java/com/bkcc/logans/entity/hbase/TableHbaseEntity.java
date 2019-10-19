package com.bkcc.logans.entity.hbase;

import com.bkcc.hbase.annotations.HBaseColumn;
import com.bkcc.hbase.annotations.HBaseRowkey;
import com.bkcc.hbase.annotations.HBaseTable;
import lombok.Data;

import java.io.Serializable;

/**
 * 【描 述】：数据库表数据实体类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-19 09:15
 */
@Data
@HBaseTable(tableName = "table", nameSpace = "logans")
public class TableHbaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 【描 述】：((模块名.表名-主键/唯一索引值)加密后第一个字符)(模块名+表名)-(主键/唯一索引值)-(时间戳升序)
     *
     *  @since  Jul 17, 2019 v1.0
     */
    @HBaseRowkey
    private String rowKey;

    /**
     * 【描 述】：数据JSON格式
     *
     *  @since 2019/10/19 09:17
     */
    @HBaseColumn
    private String data;

    /**
     * 【描 述】：修改/添加数据的执行人
     *
     *  @since 2019/10/19 09:17
     */
    @HBaseColumn
    private Long uid;

    /**
     * 【描 述】：修改/添加数据的时间
     *
     *  @since 2019/10/19 09:17
     */
    @HBaseColumn
    private String time;

 }///:~
