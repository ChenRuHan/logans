package com.bkcc.logans.controller.base;

import com.bkcc.core.util.ContextUtil;
import com.bkcc.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

/**
 * 【描 述】：所有Controller基类，包含一些常用信息对象
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Dec 24, 2018 新建
 *  @since          Dec 24, 2018 
 */
@Controller
public class BaseController{

	/**
	 * 【描 述】：缓存实例
	 *
	 *  @since  Mar 1, 2019 v1.0
	 */
	@Autowired
	protected RedisUtil redisUtil;


    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

	/**
	 * 【描 述】：获取当前登录人UID
	 *
	 * @since  Dec 24, 2018 v1.0
	 */
	protected Long getUId() {
		return ContextUtil.getCurrentUserId();
	}
}///:~
