package com.bkcc.logans.repository.hbase;

import com.bkcc.hbase.repository.AbstractHBaseRepository;
import com.bkcc.logans.entity.hbase.AnsLogHbaseEntity;
import org.springframework.stereotype.Repository;

/**
 * 【描 述】：HBase需要分析的日志数据表接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Aug 6, 2019 新建
 *  @since          Aug 6, 2019 
 */
@Repository
public class AnsLogRepository extends AbstractHBaseRepository<AnsLogHbaseEntity>{

}///:~
