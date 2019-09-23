package com.bkcc.logans.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.bkcc.logans.entity.TaskResEntity;
import com.bkcc.logans.mapper.TaskResMapper;
import com.bkcc.logans.service.TaskResService;

/**
 * 【描 述】：日志分析任务结果表业务实现类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 10:59:26
 */
@Service
public class TaskResServiceImpl implements TaskResService{
    
    /**
     * 【描 述】：日志分析任务结果表Mapper接口
     *
     *  @since  2019-09-23 10:59:26 v1.0
     */
    @Autowired
    private TaskResMapper taskResMapper;
    
    /**
     * 【描 述】：添加或修改数据
     *
     * @since 2019-09-23 10:59:26
     * @see com.bkcc.logans.service.TaskResService#insertOrUpdate(com.bkcc.logans.entity.TaskResEntity)
     */
    @Override
    public void insertOrUpdate(TaskResEntity taskRes) {
        if(taskRes.getId() == null || taskRes.getId() <= 0) {
            taskResMapper.insertTaskRes(taskRes);
        } else {
            taskResMapper.updateTaskResById(taskRes);
        }
    }

    /**
     * 【描 述】：删除日志分析任务结果表信息
     *
     * @since 2019-09-23 10:59:26
     * @see com.bkcc.logans.service.TaskResService#deleteTaskResById(java.lang.Long)
     */
    @Override
    public void deleteTaskResById(Long id) {
        taskResMapper.deleteTaskResById(id);
    }

    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @since 2019-09-23 10:59:26
     * @see com.bkcc.logans.service.TaskResService#selectTaskResList(com.bkcc.logans.entity.TaskResEntity)
     */
    @Override
    public PageInfo<TaskResEntity> selectTaskResList(TaskResEntity taskRes) {
        if(taskRes == null) {
            taskRes = new TaskResEntity();
        }
        if(taskRes.getPageNum() != null && taskRes.getPageSize() != null) {
            PageHelper.startPage(taskRes.getPageNum(), taskRes.getPageSize());
        }
        List<TaskResEntity> returnList = taskResMapper.selectTaskResList(taskRes);
        return new PageInfo<>(returnList);
    }

    /**
     * 【描 述】：查询单个日志分析任务结果表信息
     *
     * @since 2019-09-23 10:59:26
     * @see com.bkcc.logans.service.TaskResService#selectTaskResById(java.lang.Long)
     */
    @Override
    public TaskResEntity selectTaskResById(Long id) {
        return taskResMapper.selectTaskResById(id);
    }
    
}///:~
