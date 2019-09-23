package com.bkcc.logans.mapper;

import java.util.List;
import com.bkcc.logans.entity.TaskEntity;

/**
 * 【描 述】：日志分析任务配置表Mapper接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 11:00:57
 */
public interface TaskMapper {

    /**
     * 【描 述】：删除日志分析任务配置表信息
     *
     * @param id
     * @since 2019-09-23 11:00:57
     */
    void deleteTaskById(Long id);

    /**
     * 【描 述】：添加日志分析任务配置表数据
     *
     * @param task 实体类数据
     * @since 2019-09-23 11:00:57
     */
    void insertTask(TaskEntity task);

    /**
     * 【描 述】：修改日志分析任务配置表数据
     *
     * @param task 实体类数据
     * @since 2019-09-23 11:00:57
     */
    void updateTaskById(TaskEntity task);

    /**
     * 【描 述】：查询日志分析任务配置表信息列表
     *
     * @param task 实体类数据
     * @return
     * @since 2019-09-23 11:00:57
     */
    List<TaskEntity> selectTaskList(TaskEntity task);

    /**
     * 【描 述】：查询单个日志分析任务配置表信息
     *
     * @param id 主键
     * @return
     * @since 2019-09-23 11:00:57
     */
    TaskEntity selectTaskById(Long id);

}///:~