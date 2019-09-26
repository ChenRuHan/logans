package com.bkcc.logans.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.annotation.MySyncLock;
import com.bkcc.util.mytoken.exception.RRException;
import com.bkcc.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【描 述】：自定义锁注解
 * 【环 境】：J2SE 1.8
 *
 * @author 陈汝晗
 * @version v1.0 Jul 25, 2019 新建
 * @since Jul 25, 2019
 */
@Aspect
@Component
@Slf4j
public class MyLockInterceptor {

    /**
     * 【描 述】：缓存工具类
     *
     * @since Jul 25, 2019 v1.0
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 【描 述】：变量正则表达式匹配
     *
     * @since 2019/8/27 14:08
     */
    private static Pattern pattern = Pattern.compile("\\{[^\\}]*\\}");

    /**
     * 【描 述】：拦截器
     *
     * @param pjp
     * @return java.lang.Object
     * @author 陈汝晗
     * @since 2019/8/27 14:08
     */
    @Around("@annotation(com.bkcc.logans.annotation.MySyncLock)")
    @Order(Integer.MAX_VALUE - 98)
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object[] args = pjp.getArgs();
        String[] parmArr = methodSignature.getParameterNames();
        Method targetMethod = methodSignature.getMethod();
        MySyncLock ann = targetMethod.getAnnotation(MySyncLock.class);
        String key = ann.key();
        boolean debug = ann.debug();
        if (debug) {
            log.debug("# 拦截到必须同步的方法:{}.{}", targetMethod.getDeclaringClass().getName(), targetMethod.getName());
        }
        try {
            key = createKey(ann.key(), parmArr, args);
        } catch (Exception e) {
            if (debug) {
                log.error("# 动态加载同步锁key失败：{}", e.getMessage(), e);
            }
        }
        long time = ann.time() / 1000;
        if (time <= 0) {
            time = -1L;
        }
        if (debug) {
            log.debug("# key==>{}, time==>{}S", key, time);
        }
        while (redisUtil.exists(key)) {
            if (ann.exeOnce()) {
                throw new RRException("此次操作已经被拦下啦，请勿重复执行～");
            }
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                redisUtil.remove(key);
            }
        }
        redisUtil.set(key, "1", time);
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            redisUtil.remove(key);
            if (debug) {

                log.debug("# 移除同步锁key==>{}", key);
            }
        }
        return result;
    }


    private String createKey(String str, String[] parmArr, Object[] args) {
        JSONObject json = new JSONObject();
        for (int i = 0; i < args.length; i++) {
            try {
                json.put(parmArr[i], JSONObject.parseObject(JSONObject.toJSONString(args[i]), JSONObject.class));
            } catch (Exception e) {
                json.put(parmArr[i], args[i]);
            }
        }
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            String ma = m.group();
            String key = ma.replaceAll("\\{|\\}", "");
            if (json.containsKey(key)) {
                str = str.replace(ma, json.getString(key));
            } else {
                String[] arr = key.split("\\.");
                JSONObject v = null;
                Object vlu = null;
                for (int i = 0; i < arr.length; i++) {
                    String k = arr[i];
                    if (i != arr.length - 1) {
                        Object o;
                        if (v == null || v.isEmpty()) {
                            o = json.get(k);
                        } else {
                            o = v.get(k);
                        }
                        if (o instanceof String) {
                            v = JSONObject.parseObject(o.toString(), JSONObject.class);
                        } else {
                            v = (JSONObject) o;
                        }
                        continue;
                    }
                    vlu = v.getString(k);
                }
                String vv = "";
                if (vlu != null) {
                    vv = vlu.toString();
                }
                str = str.replace(ma, vv);
            }
        }
        return str;
    }
}///:~
