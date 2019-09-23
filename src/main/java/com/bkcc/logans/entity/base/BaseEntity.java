package com.bkcc.logans.entity.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 【描 述】：所有实体类基类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Dec 6, 2018 新建
 *  @since          Dec 6, 2018 
 */
@Data
public class BaseEntity implements Serializable {

	/**
	 * 【描 述】：序列化ID
	 *
	 *  @since  Dec 6, 2018 v1.0
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 【描 述】：主键ID
	 *
	 *  @since  Dec 6, 2018 v1.0
	 */
	private Long id;
	
	/**
	 * 【描 述】：创建用户UID
	 *
	 *  @since  Dec 6, 2018 v1.0
	 */
	private Long createUid;

	/**
	 * 【描 述】：当前页码
	 *
	 *  @since  Dec 6, 2018 v1.0
	 */
	private Integer pageNum;
	
	/**
	 * 【描 述】：每页大小
	 *
	 *  @since  Dec 6, 2018 v1.0
	 */
	private Integer pageSize;
	
	/**
	 * 【描 述】：修改用户UID
	 *
	 *  @since  Dec 6, 2018 v1.0
	 */
	private Long updateUid;
	
	/**
	 * 【描 述】：创建时间
	 *
	 *  @since  Jan 15, 2019 v1.0
	 */
	private String createTime;
	
	/**
	 * 【描 述】：修改时间
	 *
	 *  @since  Jan 15, 2019 v1.0
	 */
	private String updateTime;
	
}///:~
