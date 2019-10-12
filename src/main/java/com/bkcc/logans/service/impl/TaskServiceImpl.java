package com.bkcc.logans.service.impl;

import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.mapper.FieldEnumMapper;
import com.bkcc.logans.mapper.FieldMapper;
import com.bkcc.logans.mapper.TaskMapper;
import com.bkcc.logans.mapper.TaskResMapper;
import com.bkcc.logans.service.TaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【描 述】：日志分析任务配置表业务实现类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 11:00:57
 */
@Service
public class TaskServiceImpl implements TaskService{
    
    /**
     * 【描 述】：日志分析任务配置表Mapper接口
     *
     *  @since  2019-09-23 11:00:57 v1.0
     */
    @Autowired
    private TaskMapper taskMapper;
    /**
     * 【描 述】：日志分析任务结果表Mapper接口
     *
     *  @since 2019/10/12 17:14
     */
    @Autowired
    private TaskResMapper taskResMapper;
    /**
     * 【描 述】：日志分析模块字段表Mapper接口
     *
     *  @since 2019/10/12 17:14
     */
    @Autowired
    private FieldMapper fieldMapper;
    /**
     * 【描 述】：日志分析模块字段枚举表Mapper接口
     *
     *  @since 2019/10/12 17:14
     */
    @Autowired
    private FieldEnumMapper fieldEnumMapper;

    /**
     * 【描 述】：添加或修改数据
     *
     * @since 2019-09-23 11:00:57
     * @see com.bkcc.logans.service.TaskService#insertOrUpdate(com.bkcc.logans.entity.TaskEntity)
     */
    @Override
    public void insertOrUpdate(TaskEntity task) {
        if(task.getId() == null || task.getId() <= 0) {
            taskMapper.insertTask(task);
        } else {
            taskMapper.updateTaskById(task);
        }
    }

    /**
     * 【描 述】：删除日志分析任务配置表信息
     *
     * @since 2019-09-23 11:00:57
     * @see com.bkcc.logans.service.TaskService#deleteTaskById(java.lang.Long)
     */
    @Override
    public void deleteTaskById(Long id) {
        fieldEnumMapper.deleteFieldEnumByTaskId(id);
        fieldMapper.deleteFieldByTaskId(id);
        taskResMapper.deleteTaskResByTaskId(id);
        taskMapper.deleteTaskById(id);
    }

    /**
     * 【描 述】：查询日志分析任务配置表信息列表
     *
     * @since 2019-09-23 11:00:57
     * @see com.bkcc.logans.service.TaskService#selectTaskList(com.bkcc.logans.entity.TaskEntity)
     */
    @Override
    public PageInfo<TaskEntity> selectTaskList(TaskEntity task) {
        if(task == null) {
            task = new TaskEntity();
        }
        if(task.getPageNum() != null && task.getPageSize() != null) {
            PageHelper.startPage(task.getPageNum(), task.getPageSize());
        }
        List<TaskEntity> returnList = taskMapper.selectTaskList(task);
        return new PageInfo<>(returnList);
    }

    /**
     * 【描 述】：查询单个日志分析任务配置表信息
     *
     * @since 2019-09-23 11:00:57
     * @see com.bkcc.logans.service.TaskService#selectTaskById(java.lang.Long)
     */
    @Override
    public TaskEntity selectTaskById(Long id) {
        return taskMapper.selectTaskById(id);
    }
    
}///:~
