package com.bkcc.logans.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.bkcc.logans.entity.base.BaseEntity;

/**
 * 【描 述】：日志分析模块字段枚举表实体类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:28:26
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FieldEnumEntity extends BaseEntity{

    /**
     * 【描 述】：序列化UID
     *
     *  @since  Jul 15, 2019 v1.0
     */
    private static final long serialVersionUID = 1L;
    /**
     * 【描 述】：日志分析任务ID
     *
     *  @since  2019-09-20 15:28:26
     */
    private Long taskId;
    /**
     * 【描 述】：字段ID
     *
     *  @since  2019-09-20 15:28:26
     */
    private Long fieldId;
    /**
     * 【描 述】：字段对应枚举值
     *
     *  @since  2019-09-20 15:28:26
     */
    private String enumValue;
    /**
     * 【描 述】：允许范围内的值正则表达式
     *
     *  @since  2019-09-20 15:28:26
     */
    private String allowRegex;
}///:~