package com.bkcc.logans.mapper;

import java.util.List;
import com.bkcc.logans.entity.TaskResEntity;

/**
 * 【描 述】：日志分析任务结果表Mapper接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 10:59:26
 */
public interface TaskResMapper {

    /**
     * 【描 述】：删除日志分析任务结果表信息
     *
     * @param id
     * @since 2019-09-23 10:59:26
     */
    void deleteTaskResById(Long id);

    /**
     * 【描 述】：添加日志分析任务结果表数据
     *
     * @param taskRes 实体类数据
     * @since 2019-09-23 10:59:26
     */
    void insertTaskRes(TaskResEntity taskRes);

    /**
     * 【描 述】：修改日志分析任务结果表数据
     *
     * @param taskRes 实体类数据
     * @since 2019-09-23 10:59:26
     */
    void updateTaskResById(TaskResEntity taskRes);

    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @param taskRes 实体类数据
     * @return
     * @since 2019-09-23 10:59:26
     */
    List<TaskResEntity> selectTaskResList(TaskResEntity taskRes);

    /**
     * 【描 述】：查询单个日志分析任务结果表信息
     *
     * @param id 主键
     * @return
     * @since 2019-09-23 10:59:26
     */
    TaskResEntity selectTaskResById(Long id);

}///:~