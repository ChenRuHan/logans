package com.bkcc.logans.task;

import com.bkcc.logans.annotation.MySyncLock;
import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.handler.InitTaskHandler;
import com.bkcc.util.redis.RedisUtil;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 【描 述】：定时初始化系统参数
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-26 14:35
 */
@Component
public class InitTask {

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

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 【描 述】：初始化日志分析任务配置--每分钟执行一次
     *
     * @since 2019-09-20 15:23:34
     */
    @Scheduled(cron = "0 * * * * ?")
    public void initTask() {
        taskInitHandler.initTask();
    }

    /**
     * 【描 述】：初始化IP地址--每分钟执行一次
     *
     * @param
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 16:07
     */
    @Scheduled(cron = "0 * * * * ?")
    @MySyncLock(key = "logans:lock:client:ip", debug = false, exeOnce = true)
    public void initClient() {
        Application application = eurekaClient.getApplication(StringUtils.upperCase(appName));
        if (application == null) {
            return;
        }
        List<String> ipList = new ArrayList<>();
        for (InstanceInfo instance : application.getInstances()) {
            if (instance.getStatus().equals(InstanceInfo.InstanceStatus.UP)) {
                ipList.add(instance.getIPAddr() + ":" + instance.getPort());
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
        redisUtil.expire(RedisKeyConstant.IP_KEY, RedisKeyConstant.EXPIRE_TIME, TimeUnit.SECONDS);
    }

}///:~
