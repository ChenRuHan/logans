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
 * @author 陈汝晗
 * @version v1.0 新建
 * @since 2019-09-23 17:24:57
 */
@RestController
@Api(value = "日志分析任务结果表Controller")
@RequestMapping("/api/task-res")
public class TaskResController extends BaseController {

    /**
     * 【描 述】：日志分析任务结果表业务接口
     *
     * @since 2019-09-23 17:24:57
     */
    @Autowired
    private TaskResService taskResService;

    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @since 2019-09-23 17:24:57
     */
    @ApiOperation(value = "查询日志分析任务结果表信息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body", name = "taskRes", value = "{"
            + "</br> pageNum : 当前页码--不传查询全部,"
            + "</br> pageSize : 每页大小--不传查询全部"
            + "</br> orderNO : 任务执行单号,"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> beginTime : 任务分析开始时间--begin,"
            + "</br> endTime : 任务分析开始时间--end,"
            + "</br> errorCode : 错误码,"
            + "</br>}", dataType = "TaskResEntity", required = true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
            + "</br> id : 日志分析任务结果表ID,"
            + "</br> orderNO : 任务执行单号,"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> beginTime : 任务分析开始时间,"
            + "</br> endTime : 任务分析结束时间,"
            + "</br> errorCode : 错误码,"
            + "</br> resJSON : 返回结果JSON字符串"
            + "</br>}...], "
            + "</br> newPrimaryKeys : {},"
            + "</br> total:总数 </br>}")
    })
    @PostMapping("/list")
    public ViewData selectTaskResList(@RequestBody TaskResEntity taskRes) {
        if (taskRes == null) {
            return ViewData.error("传入信息有误");
        }
        PageInfo<TaskResEntity> pageInfo = taskResService.selectTaskResList(taskRes);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }

    /**
     * 【描 述】：通过订单号查询日志分析结果
     *
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "通过订单号查询日志分析结果")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "orderNO", value = "订单号", dataType = "Long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "{</br> rows:[{"
                    + "</br> orderNO : 任务执行单号,"
                    + "</br> taskId : 日志分析任务ID,"
                    + "</br> beginTime : 任务分析开始时间,"
                    + "</br> endTime : 任务分析结束时间,"
                    + "</br> errorCode : 错误码,"
                    + "</br> resJSON : 返回结果JSON字符串"
                    + "</br>}], </br> newPrimaryKeys : {}"
                    + "</br>}")
    })
    @GetMapping("/{orderNO}/detail")
    public ViewData selectTaskResByOrderNO(@PathVariable Long orderNO) {
        TaskResEntity par = new TaskResEntity();
        par.setOrderNO(orderNO);
        return selectTaskResList(par);
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
