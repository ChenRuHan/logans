package com.bkcc.logans.service.impl;

import com.bkcc.logans.entity.TaskResEntity;
import com.bkcc.logans.entity.hbase.AnsResHbaseEntity;
import com.bkcc.logans.mapper.TaskResMapper;
import com.bkcc.logans.repository.hbase.AnsResRepository;
import com.bkcc.logans.service.TaskResService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【描 述】：日志分析任务结果表业务实现类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 17:24:57
 */
@Service
public class TaskResServiceImpl implements TaskResService{
    
    /**
     * 【描 述】：日志分析任务结果表Mapper接口
     *
     *  @since  2019-09-23 17:24:57 v1.0
     */
    @Autowired
    private TaskResMapper taskResMapper;

    /**
     * 【描 述】：HBASE日志分析结果接口
     *
     *  @since 2019/10/14 11:06
     */
    @Autowired
    private AnsResRepository ansResRepository;

    /**
     * 【描 述】：添加或修改数据
     *
     * @since 2019-09-23 17:24:57
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
     * @since 2019-09-23 17:24:57
     * @see com.bkcc.logans.service.TaskResService#deleteTaskResById(java.lang.Long)
     */
    @Override
    public void deleteTaskResById(Long id) {
        taskResMapper.deleteTaskResById(id);
    }

    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @since 2019-09-23 17:24:57
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
        for (TaskResEntity taskResEntity : returnList) {
            setResJSON(taskResEntity);
        }
        return new PageInfo<>(returnList);
    }


    /**
     * 【描 述】：查询单个日志分析任务结果表信息
     *
     * @since 2019-09-23 17:24:57
     * @see com.bkcc.logans.service.TaskResService#selectTaskResById(java.lang.Long)
     */
    @Override
    public TaskResEntity selectTaskResById(Long id) {
        TaskResEntity taskResEntity = taskResMapper.selectTaskResById(id);
        setResJSON(taskResEntity);
        return taskResEntity;
    }

    /**
     * 【描 述】：通过订单号查询日志分析结果
     *
     * @param orderNO 订单编号
     * @return com.bkcc.logans.entity.hbase.AnsResHbaseEntity
     * @author 陈汝晗
     * @since 2019/10/14 11:05
     */
    @Override
    public AnsResHbaseEntity selectTaskResByOrderNO(Long orderNO) {
        AnsResHbaseEntity ans =  ansResRepository.get(StringUtils.reverse(orderNO+""));
        if (ans == null) {
            ans = new AnsResHbaseEntity();
        }
        return ans;
    }

    private void setResJSON(TaskResEntity taskResEntity) {
        if (taskResEntity == null) {
            return;
        }
        AnsResHbaseEntity df = selectTaskResByOrderNO(taskResEntity.getOrderNO());
        if (df == null) {
            return;
        }
        taskResEntity.setResJSON(df.getRes());
    }
}///:~
