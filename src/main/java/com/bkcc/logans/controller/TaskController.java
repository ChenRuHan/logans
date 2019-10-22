package com.bkcc.logans.controller;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.core.data.ViewData;
import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.constant.LogansMQConstant;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.controller.base.BaseController;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.enums.TaskStatusEnum;
import com.bkcc.logans.handler.TaskHandler;
import com.bkcc.logans.service.TaskService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * 【描 述】：日志分析任务配置表Controller
 * 【环 境】：J2SE 1.8
 *
 * @author 陈汝晗
 * @version v1.0 新建
 * @since 2019-09-23 11:00:57
 */
@RestController
@Api(value = "日志分析任务配置表Controller")
@RequestMapping("/api/task")
public class TaskController extends BaseController {

    /**
     * 【描 述】：日志分析任务配置表业务接口
     *
     * @since 2019-09-23 11:00:57
     */
    @Autowired
    private TaskService taskService;

    /**
     * 【描 述】：消息通知
     *
     * @since Aug 16, 2019 v1.0
     */
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    /**
     * 【描 述】：查询日志分析任务配置表信息列表
     *
     * @since 2019-09-23 11:00:57
     */
    @ApiOperation(value = "查询日志分析任务配置表信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "task", value = "{"
                    + "</br> pageNum : 当前页码--不传查询全部,"
                    + "</br> pageSize : 每页大小--不传查询全部"
                    + "</br> taskName : 分析任务中文名称,"
                    + "</br> moduleName : 微服务模块名称,"
                    + "</br> reqMethod : 需要分析请求的方法 GET POST DELETE PUT,"
                    + "</br> reqUri : 需要分析请求的URI,"
                    + "</br> ansType : 分析类型，1--不分析直接输出指定字段，2--通过指定字段聚合分析,"
                    + "</br> ansRateType : 分析频次，每（1--分，2--时，3--日，4--月）分析一次。,"
                    + "</br> outQueue : 分析结果输出消息队列名称,"
                    + "</br>}", dataType = "TaskEntity", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "{</br> rows:[{"
                    + "</br> id : 日志分析任务配置表ID,"
                    + "</br> taskName : 分析任务中文名称,"
                    + "</br> moduleName : 微服务模块名称,"
                    + "</br> reqMethod : 需要分析请求的方法 GET POST DELETE PUT,"
                    + "</br> reqUri : 需要分析请求的URI,"
                    + "</br> ansType : 分析类型，1--不分析直接输出指定字段，2--通过指定字段聚合分析,"
                    + "</br> ansRateType : 分析频次，每（1--分，2--时，3--日，4--月）分析一次。,"
                    + "</br> outQueue : 分析结果输出消息队列名称,"
                    + "</br>}...], "
                    + "</br> newPrimaryKeys : {},"
                    + "</br> total:总数 </br>}")
    })
    @PostMapping("/list")
    public ViewData selectTaskList(@RequestBody TaskEntity task) {
        if (task == null) {
            return ViewData.error("传入信息有误");
        }
        PageInfo<TaskEntity> pageInfo = taskService.selectTaskList(task);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }

    /**
     * 【描 述】：添加、修改日志分析任务配置表信息
     *
     * @since 2019-09-23 11:00:57
     */
    @ApiOperation(value = "添加、修改日志分析任务配置表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "task", value = "{"
                    + "</br> \"id\" : 主键ID，不传或-1代表新增,"
                    + "</br> \"taskName\" : 分析任务中文名称,"
                    + "</br> \"moduleName\" : 微服务模块名称,"
                    + "</br> \"reqMethod\" : 需要分析请求的方法 GET POST DELETE PUT,"
                    + "</br> \"reqUri\" : 需要分析请求的URI,"
                    + "</br> \"ansType\" : 分析类型，1--不分析直接输出指定字段，2--通过指定字段聚合分析,"
                    + "</br> \"ansRateType\" : 分析频次，每（1--分，2--时，3--日，4--月）分析一次。,"
                    + "</br> \"outQueue\" : 分析结果输出消息队列名称,"
                    + "</br>}", dataType = "TaskEntity", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "{</br>newPrimaryKeys : {"
                    + "</br> id : 日志分析任务配置表ID"
                    + "</br>}</br>}")
    })
    @PostMapping
    public ViewData insertOrUpdate(@RequestBody TaskEntity task) {
        if (task == null) {
            return ViewData.error("传入信息有误");
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatusEnum.OPEN.getValue());
        }
        boolean isAdd = false;
        if (task.getId() == null || task.getId() <= 0) {
            isAdd = true;
        }
        taskService.insertOrUpdate(task);

        Map<String, TaskEntity> taskMap = new HashMap<>();
        if (isAdd) {
            if (TaskStatusEnum.OPEN.equels(task.getStatus())) {
                taskMap.put(TaskConstant.INSERT_STATUS, task);
            }
        } else {
            if (TaskStatusEnum.CLOSE.equels(task.getStatus())) {
                taskMap.put(TaskConstant.DELETE_STATUS, task);
            } else if (TaskStatusEnum.OPEN.equels(task.getStatus())) {
                taskMap.put(TaskConstant.UPDATE_STATUS, task);
            }
        }
        Destination destination = new ActiveMQTopic(LogansMQConstant.LOGANS_TASK);
        jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(taskMap));
        return ViewData.put("id", task.getId());
    }

    /**
     * 【描 述】：删除日志分析任务配置表信息
     *
     * @param taskId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "删除日志分析任务配置表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "taskId", value = "日志分析任务配置表ID", dataType = "Long", required = true)
    })
    @DeleteMapping("/{taskId}/delete")
    public ViewData deleteById(@PathVariable Long taskId) {
        TaskEntity task = taskService.selectTaskById(taskId);
        if (task == null) {
            return ViewData.ok();
        }
        taskService.deleteTaskById(taskId);
        Map<String, TaskEntity> taskMap = new HashMap<>();
        taskMap.put(TaskConstant.DELETE_STATUS, task);
        Destination destination = new ActiveMQTopic(LogansMQConstant.LOGANS_TASK);
        jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(taskMap));
        return ViewData.ok();
    }

    /**
     * 【描 述】：查询单个日志分析任务配置表信息
     *
     * @param taskId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "查询单个日志分析任务配置表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "taskId", value = "日志分析任务配置表ID", dataType = "Long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "{</br> rows:[{"
                    + "</br> taskName : 分析任务中文名称,"
                    + "</br> moduleName : 微服务模块名称,"
                    + "</br> reqMethod : 需要分析请求的方法 GET POST DELETE PUT,"
                    + "</br> reqUri : 需要分析请求的URI,"
                    + "</br> ansType : 分析类型，1--不分析直接输出指定字段，2--通过指定字段聚合分析,"
                    + "</br> ansRateType : 分析频次，每（1--分，2--时，3--日，4--月）分析一次。,"
                    + "</br> outQueue : 分析结果输出消息队列名称,"
                    + "</br>}], </br> newPrimaryKeys : {}"
                    + "</br>}")
    })
    @GetMapping("/{taskId}/detail")
    public ViewData selectTaskById(@PathVariable Long taskId) {
        TaskEntity taskEntity = taskService.selectTaskById(taskId);
        return ViewData.ok(taskEntity);
    }


    /**
     * 【描 述】：展示页面
     *
     * @return
     * @since Mar 20, 2019
     */
    @GetMapping("/view")
    public ModelAndView returnView() {
        ModelAndView m = new ModelAndView();
        m.setViewName("task/Task");
        return m;
    }


    /**
     * 【描 述】：查询信息列表
     *
     * @since 2019-07-29 15:31:06
     */
    @PostMapping("/list4view")
    public ViewData selectFeedbackList(Integer page, Integer rows) {
        TaskEntity systemNoticeEntity = new TaskEntity();
        systemNoticeEntity.setPageNum(page);
        systemNoticeEntity.setPageSize(rows);
        PageInfo<TaskEntity> pageInfo = taskService.selectTaskList(systemNoticeEntity);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }


    @Autowired
    @Qualifier("pollTaskDispatch")
    private AbstractTaskDispatch taskDispatch;

    @Autowired
    @Qualifier("logansTaskActuator")
    private AbstractTaskActuator actuator;


    @GetMapping("/{taskId}/trigger")
    public ViewData trigger(@PathVariable Long taskId) {
        actuator.setTaskId(taskId);
        TaskHandler taskHandler = new TaskHandler(taskDispatch, actuator);
        new Thread(taskHandler).start();
        return ViewData.ok();
    }


}///：～
