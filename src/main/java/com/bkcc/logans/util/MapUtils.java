package com.bkcc.logans.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 【描 述】：Map工具类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Apr 29, 2019 新建
 *  @since          Apr 29, 2019 
 */
public class MapUtils {
	
	
	/**
	 * 【描 述】：创建一个Map
	 *
	 * @param key
	 * @param valueObj
	 * @return
	 * @since Apr 3, 2019
	 */
	public static Map<?, ?> emptyMap(){
		return new HashMap<>();
	}
	
	/**
	 * 【描 述】：创建一个Map
	 *
	 * @param key
	 * @param valueObj
	 * @return
	 * @since Apr 3, 2019
	 */
	public static Map<String, Object> createNewMap(String key, Object valueObj){
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put(key, valueObj);
		return returnMap;
	}
	
	/**
	 * 【描 述】：创建一个Map
	 *
	 * @param key
	 * @param valueStr
	 * @return
	 * @since Apr 3, 2019
	 */
	public static Map<String, String> createNewMap(String key, String valueStr){
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put(key, valueStr);
		return returnMap;
	}

	private MapUtils() {}
}///:!
