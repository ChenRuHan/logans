package com.bkcc.logans.dispatch;

import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 【描 述】：轮询式任务调度器，任务轮询在每台机器中执行
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 10:08
 */
@Slf4j
@Component
public class PollTaskDispatch extends AbstractTaskDispatch {


    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.cloud.client.ipAddress}")
    private String ip;

    @Value("${server.port}")
    private String port;

    /**
     * 【描 述】：轮询算法
     *
     * @return boolean
     * @author 陈汝晗
     * @since 2019/9/21 10:20
     */
    @Override
    public boolean canExecute(Long taskId) {
        /*
           解决多个服务器时间差问题。判断任务在当前时间段是否已经执行
         */
        String now = FastDateFormat.getInstance("yyyyMMddHHmm").format(new Date());
        String lastExeTimeKey = RedisKeyConstant.TASK_LAST_EXE_TIME + taskId;
        String lastExeTime = redisUtil.getString(lastExeTimeKey);
        if (StringUtils.equals(lastExeTime, now)) {
            return false;
        }
        redisUtil.set(lastExeTimeKey, now);

        String lastExeIp = (String) redisUtil.hmGet(RedisKeyConstant.TASK_KEY, taskId);
        String nextIp = "";
        if (StringUtils.isBlank(lastExeIp)) {
            Set<Object> keys = redisUtil.hmKeys(RedisKeyConstant.IP_KEY);
            for (Object key : keys) {
                nextIp = key.toString();
                break;
            }
        } else {
            nextIp = (String) redisUtil.hmGet(RedisKeyConstant.IP_KEY, lastExeIp);
            /*
                如果查询不到，证明该节点已经下线，重新轮询。
             */
            if (StringUtils.isBlank(nextIp)) {
                Set<Object> keys = redisUtil.hmKeys(RedisKeyConstant.IP_KEY);
                for (Object key : keys) {
                    nextIp = key.toString();
                    break;
                }
            }
        }
        if (StringUtils.equals(nextIp, ip + ":" + port)) {
            return true;
        }
        return false;
    }


    /**
     * 【描 述】：执行完毕之后执行
     *
     * @param taskId
     * @return void
     * @author 陈汝晗
     * @since 2019/10/22 14:10
     */
    @Override
    public void afterExecute(Long taskId) {
        redisUtil.hmSet(RedisKeyConstant.TASK_KEY, taskId, ip + ":" + port);
        redisUtil.expire(RedisKeyConstant.TASK_KEY, RedisKeyConstant.EXPIRE_TIME * 60, TimeUnit.SECONDS);
    }


}///:~
