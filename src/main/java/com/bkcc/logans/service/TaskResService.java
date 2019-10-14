package com.bkcc.logans.service;

import com.bkcc.logans.entity.TaskResEntity;
import com.bkcc.logans.entity.hbase.AnsResHbaseEntity;
import com.github.pagehelper.PageInfo;

/**
 * 【描 述】：日志分析任务结果表业务接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 17:24:57
 */
public interface TaskResService {

    /**
     * 【描 述】：添加或修改数据
     *
     * @param taskRes
     * @since Jul 15, 2019
     */
    void insertOrUpdate(TaskResEntity taskRes);

    /**
     * 【描 述】：删除日志分析任务结果表信息
     *
     * @param id 主键
     * @since Jul 15, 2019
     */
    void deleteTaskResById(Long id);

    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @param phoneBook
     * @return
     * @since Jul 15, 2019
     */
    PageInfo<TaskResEntity> selectTaskResList(TaskResEntity taskRes);

    /**
     * 【描 述】：查询单个日志分析任务结果表信息
     *
     * @param id 主键
     * @return
     * @since Jul 15, 2019
     */
    TaskResEntity selectTaskResById(Long id);

    /**
     * 【描 述】：通过订单号查询日志分析结果
     *
     * @param orderNO 订单编号
     * @return com.bkcc.logans.entity.hbase.AnsResHbaseEntity
     * @author 陈汝晗
     * @since 2019/10/14 11:05
     */
    AnsResHbaseEntity selectTaskResByOrderNO(Long orderNO);
}///:~
