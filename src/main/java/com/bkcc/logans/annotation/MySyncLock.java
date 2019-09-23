package com.bkcc.logans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 【描 述】：自定义分布式同步锁
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Jul 25, 2019 新建
 *  @since          Jul 25, 2019 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MySyncLock {
	
	/**
	 * 【描 述】：同步锁Key，加此锁代表分布式函数或接口同步执行，锁尽量加到最小范围。</br>
	 * 			命名规范：<微服务name>:lock:[业务key]:...</br>
	 * 			eg:积分兑换同步锁key==>jczl:lock:goods:{goods.id}</br>
	 * 			动态变量定义规则{函数变量名称.变量值.变量值...} eg: {goods.id}
	 * @return
	 * @since Jul 25, 2019
	 */
	String key();
	
	/**
	 * 【描 述】：加锁最长时间毫秒，默认60S，<=0代表无限制时间，知道任务执行完为止
	 *
	 * @return
	 * @since Jul 25, 2019
	 */
	long time() default 60000L;
	
	/**
	 * 【描 述】：单位时间内是否就执行一次
	 *
	 * @return
	 * @since Jul 31, 2019
	 */
	boolean exeOnce() default false;

	/**
	 * 【描 述】：是否打印日志
	 *
	 *  @since 2019/9/21 16:20
	 */
	boolean debug() default true;

}///:~
