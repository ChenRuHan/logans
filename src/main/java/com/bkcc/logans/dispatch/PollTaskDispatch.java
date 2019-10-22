package com.bkcc.logans.dispatch;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.constant.TaskConstant;
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
        String lastExeTimeKey = RedisKeyConstant.TASK_EXE_KEY + taskId;
        JSONObject lastJson = new JSONObject();
        String lastJsonStr = redisUtil.getString(lastExeTimeKey);
        if (StringUtils.isNotBlank(lastJsonStr)) {
            lastJson = JSONObject.parseObject(lastJsonStr);
        }
        String lastExeTime = lastJson.getString("time");
        if (StringUtils.equals(lastExeTime, now)) {
            return false;
        }
        String lastExeIpAndPort = lastJson.getString("ipPort");
        String nextCanExeIpPort = "";
        if (StringUtils.isBlank(lastExeIpAndPort)) {
            Set<Object> keys = redisUtil.hmKeys(RedisKeyConstant.IP_KEY);
            for (Object key : keys) {
                nextCanExeIpPort = key.toString();
                break;
            }
        } else {
            nextCanExeIpPort = (String) redisUtil.hmGet(RedisKeyConstant.IP_KEY, lastExeIpAndPort);
            /*
                如果查询不到，证明该节点已经下线，重新轮询。
             */
            if (StringUtils.isBlank(nextCanExeIpPort)) {
                Set<Object> keys = redisUtil.hmKeys(RedisKeyConstant.IP_KEY);
                for (Object key : keys) {
                    nextCanExeIpPort = key.toString();
                    break;
                }
            }
        }
        String currIpPort = ip + ":" + port;
        if (StringUtils.equals(nextCanExeIpPort, currIpPort)) {
            lastJson.put("time", now);
            lastJson.put("ipPort", currIpPort);
            redisUtil.set(lastExeTimeKey, lastJson.toJSONString(), TaskConstant.getExpireTime(taskId));
            return true;
        }
        return false;
    }

}///:~
