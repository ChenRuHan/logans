package com.bkcc.logans.controller;

import com.bkcc.core.data.ViewData;
import com.bkcc.logans.annotation.MySyncLock;
import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.handler.InitTaskHandler;
import com.bkcc.util.redis.RedisUtil;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 【描 述】：初始化任务控制器
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 11:10
 */
@RestController
@Api(value = "初始化任务控制器")
@RequestMapping("/api/init")
public class InitTaskController {

    /**
     * 【描 述】：任务初始化执行器
     *
     * @since 2019/9/20 17:17
     */
    @Autowired
    private InitTaskHandler taskInitHandler;


    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 【描 述】：初始化日志分析任务配置
     *
     * @since 2019-09-20 15:23:34
     */
    @ApiOperation(value = "初始化日志分析任务配置--默认每分钟执行一次")
    @Scheduled(cron = "0 * * * * ?")
    @PostMapping("/task")
    public ViewData initTask() {
        taskInitHandler.initTask();
        return ViewData.ok();
    }

    /**
     * 【描 述】：初始化
     *
     * @param
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 16:07
     */
    @Scheduled(cron = "0 * * * * ?")
    @MySyncLock(key="logans:lock:client", debug = false)
    public void initClient() {
        Application application = eurekaClient.getApplication("LOGANS");
        if (application == null) {
            return;
        }
        List<String> ipList = new ArrayList<>();
        for (InstanceInfo instance : application.getInstances()) {
            if (instance.getStatus().equals(InstanceInfo.InstanceStatus.UP)) {
                ipList.add(instance.getIPAddr());
            }
        }
        if (ipList.isEmpty()) {
            return;
        }
        redisUtil.remove(RedisKeyConstant.IP_KEY);
        for (int i = 0; i < ipList.size(); i++) {
            String cur = ipList.get(i);
            String next;
            if (i == ipList.size() - 1) {
                next = ipList.get(0);
            } else {
                next = ipList.get(i + 1);
            }
            redisUtil.hmSet(RedisKeyConstant.IP_KEY, cur, next);
        }
        redisUtil.expire(RedisKeyConstant.IP_KEY, 60, TimeUnit.SECONDS);

    }
}///:~
