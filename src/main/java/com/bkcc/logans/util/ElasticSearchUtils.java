package com.bkcc.logans.util;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.entity.TaskEntity;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【描 述】：ElasticSearch工具类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Apr 29, 2019 新建
 *  @since          Apr 29, 2019 
 */
@Slf4j
public class ElasticSearchUtils {

	/**
	 * 【描 述】：通过ip和host获取访问Esurl
	 *
	 * @param ip
	 * @param port
	 * @return
	 * @since May 6, 2019
	 */
	public static String getSimpleLogEsUrl(String moduleName, String ip, Integer port) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String date = sdf.format(new Date());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		String ye = sdf.format(cal.getTime());
		if(!ye.equals(date)) {
			date = ye;
		}
		return "http://" + ip + ":" + port + "/bkcc-"+moduleName+"-simplelog-" + date + "/_search";
	}
	
	
	/**
	 * 【描 述】：查询数据
	 *
	 * @param url
	 * @return
	 * @since Apr 29, 2019
	 */
	public static List<JSONObject> querySimpleLogList(String url) {
		JSONObject res = JSONObject.parseObject(MyHttpUtil.sendGet(url));
		log.debug("# 通过ES查询简要日志\nurl==>{}\nres==>{}",  url, res);
		return changeRes2List(res);
	}
	
	/**
	 * 【描 述】：查询数据
	 *
	 * @param url
	 * @param params
	 * @return
	 * @since Apr 29, 2019
	 */
	public static List<JSONObject> querySimpleLogList(String url, Map<String, Object> params) {
	    long l = System.currentTimeMillis();
		JSONObject res = JSONObject.parseObject(MyHttpUtil.sendPostJson(params, url));
		log.debug("# 通过ES查询简要日志\nurl==>{}\nparamMap==>{}\nres==>{}\n耗时==>{}毫秒", url ,JSONObject.toJSONString(params), res, (System.currentTimeMillis() - l));
		return changeRes2List(res);
	}

	/*
	 *  ================================================ 私有方法 =============================================
	 */
	/**
	 * 【描 述】：将ES返回数据集合转变成集合对象
	 *
	 * @param res
	 * @return
	 * @since May 6, 2019
	 */
	private static List<JSONObject> changeRes2List(JSONObject res){
		List<JSONObject> returnList = new ArrayList<>();
		try {
			List<String> list = JSONObject.parseArray(JSONObject.parseObject(res.get("hits").toString()).get("hits").toString(), String.class);
			for(String str : list) {
                JSONObject s = JSONObject.parseObject(JSONObject.parseObject(str).get("_source").toString(), JSONObject.class);
				returnList.add(s);
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return returnList;
	}

    public static Map<String, Object> createParam(TaskEntity taskEntity, String[] source) {
        Map<String, Object> timeRangeMap = new HashMap<>();
        timeRangeMap.put("gte", taskEntity.getBeginTime());
        timeRangeMap.put("lte", taskEntity.getEndTime());
        Map<String, Object> boolMap = new HashMap<>();
        List<Map<String, Object>> mustList = new ArrayList<>();
        mustList.add(MapUtils.createNewMap("match", MapUtils.createNewMap("uri.keyword", taskEntity.getReqUri())));
        mustList.add(MapUtils.createNewMap("match", MapUtils.createNewMap("req.keyword", taskEntity.getReqMethod())));
        boolMap.put("must", mustList);
        boolMap.put("filter", MapUtils.createNewMap("range", MapUtils.createNewMap("timestamp.keyword", timeRangeMap)));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bool", boolMap);
        Map<String, Object> queryMap = MapUtils.createNewMap("query", paramMap);
        queryMap.put("_source", source);
        queryMap.put("from", 0);
        queryMap.put("size", Integer.MAX_VALUE);
        return queryMap;
    }
}///:~
