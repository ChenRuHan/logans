package com.bkcc.logans.service;

import com.bkcc.logans.entity.TaskEntity;
import com.github.pagehelper.PageInfo;

/**
 * 【描 述】：日志分析任务配置表业务接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 11:00:57
 */
public interface TaskService {

    /**
     * 【描 述】：添加或修改数据
     *
     * @param task
     * @since Jul 15, 2019
     */
    void insertOrUpdate(TaskEntity task);

    /**
     * 【描 述】：删除日志分析任务配置表信息
     *
     * @param id 主键
     * @since Jul 15, 2019
     */
    void deleteTaskById(Long id);

    /**
     * 【描 述】：查询日志分析任务配置表信息列表
     *
     * @param phoneBook
     * @return
     * @since Jul 15, 2019
     */
    PageInfo<TaskEntity> selectTaskList(TaskEntity task);

    /**
     * 【描 述】：查询单个日志分析任务配置表信息
     *
     * @param id 主键
     * @return
     * @since Jul 15, 2019
     */
    TaskEntity selectTaskById(Long id);


}///:~
