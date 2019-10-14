package com.bkcc.logans.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.bkcc.logans.entity.base.BaseEntity;

/**
 * 【描 述】：日志分析任务结果表实体类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 17:24:57
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class TaskResEntity extends BaseEntity{

    /**
     * 【描 述】：序列化UID
     *
     *  @since  Jul 15, 2019 v1.0
     */
    private static final long serialVersionUID = 1L;
    /**
     * 【描 述】：任务执行单号
     *
     *  @since  2019-09-23 17:24:57
     */
    private Long orderNO;
    /**
     * 【描 述】：日志分析任务ID
     *
     *  @since  2019-09-23 17:24:57
     */
    private Long taskId;
    /**
     * 【描 述】：任务执行开始时间
     *
     *  @since  2019-09-23 17:24:57
     */
    private String beginTime;
    /**
     * 【描 述】：任务执行结束时间
     *
     *  @since  2019-09-23 17:24:57
     */
    private String endTime;
    /**
     * 【描 述】：错误码
     *
     *  @since  2019-09-23 17:24:57
     */
    private Integer errorCode;

    /**
     * 【描 述】：分析结果JSON字符串
     *
     *  @since 2019/10/14 11:11
     */
    private String resJSON;
}///:~