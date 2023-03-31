package com.zjun122.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;


public class SnowflakeUtils {

//    Log log = new Log4jLog(IdGeneratorSnowflake.class);

    // 工作设备号
    private static long workerID= 0;
    // 服务 编号
    private static long datacenterID=1;

    // 创建雪花算法对象
    private static Snowflake snowflake = IdUtil.createSnowflake(workerID,datacenterID);


    public SnowflakeUtils(){
        try{
            //获取机器的ipV4 转换成 long 型
            workerID = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
//            log.info("当前机器的workerID:{}",workerID);
        }catch (Exception e){
            e.printStackTrace();
//            log.error("当前机器的workerID获取失败",e);
            workerID = NetUtil.getLocalhostStr().hashCode();
        }
    }

    public static synchronized long snowflakeId(){
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(SnowflakeUtils.snowflakeId());
    }
}


