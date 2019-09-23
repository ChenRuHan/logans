package com.bkcc.logans.dispatch;

import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.logans.util.ComputerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 【描 述】：轮询算法
     *
     * @return boolean
     * @author 陈汝晗
     * @since 2019/9/21 10:20
     */
    @Override
    public boolean canExecute(Long taskId) {
        HashOperations<String, String, String> redisHash = redisTemplate.opsForHash();
        String lastExeIp = redisHash.get(RedisKeyConstant.TASK_KEY, taskId + "");
        String nextIp = "";
        if (StringUtils.isBlank(lastExeIp)) {
            Set<String> keys = redisHash.keys(RedisKeyConstant.IP_KEY);
            for (String key : keys) {
                nextIp = key;
                continue;
            }
        } else {
            nextIp = redisHash.get(RedisKeyConstant.IP_KEY, lastExeIp);
            /*
                如果查询不到，证明该节点已经下线，重新轮询。
             */
            if (StringUtils.isBlank(nextIp)) {
                Set<String> keys = redisHash.keys(RedisKeyConstant.IP_KEY);
                for (String key : keys) {
                    nextIp = key;
                    continue;
                }
            }
        }
        Set<String> ipSet = ComputerUtils.getIPSet();
        if (ipSet.contains(nextIp)) {
            redisHash.put(RedisKeyConstant.TASK_KEY, taskId + "", nextIp);
            return true;
        }
        return false;
    }

}///:~
