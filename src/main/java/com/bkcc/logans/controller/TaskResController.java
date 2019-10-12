package com.bkcc.logans.controller;

import com.bkcc.core.data.ViewData;
import com.bkcc.logans.controller.base.BaseController;
import com.bkcc.logans.entity.TaskResEntity;
import com.bkcc.logans.service.TaskResService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【描 述】：日志分析任务结果表Controller
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 17:24:57
 */
@RestController
@Api(value = "日志分析任务结果表Controller")
@RequestMapping("/api/task-res")
public class TaskResController extends BaseController{

    /**
     * 【描 述】：日志分析任务结果表业务接口
     *
     *  @since  2019-09-23 17:24:57
     */
    @Autowired
    private TaskResService taskResService;
    
    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @since  2019-09-23 17:24:57
     */
    @ApiOperation(value = "查询日志分析任务结果表信息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name="taskRes", value="{"
            + "</br> pageNum : 当前页码--不传查询全部,"
            + "</br> pageSize : 每页大小--不传查询全部"
            + "</br> orderNO : 任务执行单号,"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> beginTime : 任务执行开始时间,"
            + "</br> endTime : 任务执行结束时间,"
            + "</br> errorCode : 错误码,"
            + "</br>}", dataType="TaskResEntity", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
            + "</br> id : 日志分析任务结果表ID,"
            + "</br> orderNO : 任务执行单号,"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> beginTime : 任务执行开始时间,"
            + "</br> endTime : 任务执行结束时间,"
            + "</br> errorCode : 错误码,"
            + "</br>}...], "
            + "</br> newPrimaryKeys : {},"
            + "</br> total:总数 </br>}")
    })
    @PostMapping("/list")
    public ViewData selectTaskResList(@RequestBody TaskResEntity taskRes) {
        if(taskRes == null) {
            return ViewData.error("传入信息有误");
        }
        PageInfo<TaskResEntity> pageInfo = taskResService.selectTaskResList(taskRes);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }
            

    /**
     * 【描 述】：添加、修改日志分析任务结果表信息
     *
     * @since  2019-09-23 17:24:57
     */
    @ApiOperation(value = "添加、修改日志分析任务结果表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name="taskRes", value="{"
                + "</br> \"id\" : 主键ID，不传或-1代表新增,"
                + "</br> \"orderNO\" : 任务执行单号,"
                + "</br> \"taskId\" : 日志分析任务ID,"
                + "</br> \"beginTime\" : 任务执行开始时间,"
                + "</br> \"endTime\" : 任务执行结束时间,"
                + "</br> \"errorCode\" : 错误码,"
                + "</br>}", dataType="TaskResEntity", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br>newPrimaryKeys : {"
            + "</br> id : 日志分析任务结果表ID"
            + "</br>}</br>}" )
    })
    @PostMapping
    public ViewData insertOrUpdate(@RequestBody TaskResEntity taskRes) {
        if(taskRes == null) {
            return ViewData.error("传入信息有误");
        }
        taskResService.insertOrUpdate(taskRes);
        return ViewData.put("id", taskRes.getId());
    }
    
    /**
     * 【描 述】：删除日志分析任务结果表信息
     *
     * @param taskResId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "删除日志分析任务结果表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path", name="taskResId", value="日志分析任务结果表ID", dataType="Long", required=true)
    })
    @DeleteMapping("/{taskResId}/delete")
    public ViewData deleteById(@PathVariable Long taskResId) {
        taskResService.deleteTaskResById(taskResId);
        return ViewData.ok();
    }
    
    /**
     * 【描 述】：查询单个日志分析任务结果表信息
     *
     * @param taskResId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "查询单个日志分析任务结果表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path", name="taskResId", value="日志分析任务结果表ID", dataType="Long", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
                + "</br> orderNO : 任务执行单号,"
                + "</br> taskId : 日志分析任务ID,"
                + "</br> beginTime : 任务执行开始时间,"
                + "</br> endTime : 任务执行结束时间,"
                + "</br> errorCode : 错误码,"
            + "</br>}], </br> newPrimaryKeys : {}"
            + "</br>}" )
    })
    @GetMapping("/{taskResId}/detail")
    public ViewData selectTaskResById(@PathVariable Long taskResId) {
        TaskResEntity taskResEntity = taskResService.selectTaskResById(taskResId);
        return ViewData.ok(taskResEntity);
    }


    /**
     * 【描 述】：查询信息列表
     *
     * @since 2019-07-29 15:31:06
     */
    @PostMapping("/list4view")
    public ViewData selectFeedbackList(Integer page, Integer rows, Long taskId) {
        TaskResEntity taskRes = new TaskResEntity();
        taskRes.setPageNum(page);
        taskRes.setPageSize(rows);
        taskRes.setTaskId(taskId);
        PageInfo<TaskResEntity> pageInfo = taskResService.selectTaskResList(taskRes);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }


}///：～
