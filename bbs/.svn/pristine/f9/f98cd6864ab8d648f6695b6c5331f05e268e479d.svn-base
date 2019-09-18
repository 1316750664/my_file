package com.ctd.util.database;

import com.util.tools.ReadWriteProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hzm on 2014/7/8.
 */
public class DSPoolFactory {
    private static final long CHECK_TIME = 30 * 60 * 1000L;//间隔30分钟检测一次服务器在线情况
    private static final int TIMEOUT = 30000;//超出30秒无响应则认为此机器宕机，将从数据源服务器中移除
    private static String writeDataSourcePoolId;
    private static LinkedList<String> readDataSourcePoolIds;
    private static Map<String, String> dataSourceAddresses;
    private static Timer timer;

    static {
        dataSourceAddresses = new HashMap<String, String>();
        ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
        writeDataSourcePoolId = propertiesUtil.readValue("service", "readAndWritePool");
//        System.out.println("writeDataSourcePoolId:" + writeDataSourcePoolId);
        String writeAddress = propertiesUtil.readValue("service", writeDataSourcePoolId);
        dataSourceAddresses.put(writeDataSourcePoolId, writeAddress);
        readDataSourcePoolIds = new LinkedList<String>();
        String readPools = propertiesUtil.readValue("service", "readOnlyPool");
//        System.out.println("readOnlyPool:" + readPools);
        if (readPools == null || "".equals(readPools)) {
            readPools = writeDataSourcePoolId;
        }
        String[] pools = readPools.split(",");
        for (String pool : pools) {
            if (pool == null || "".equals(pool)) {
                continue;
            }
            readDataSourcePoolIds.add(pool);
            String address = propertiesUtil.readValue("service", pool);
            dataSourceAddresses.put(pool, address);
        }
        timer = new Timer("Timer-web-ds", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Iterator<Map.Entry<String, String>> iterator = dataSourceAddresses.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    try {
                        InetAddress.getByName(value).isReachable(TIMEOUT);
                        if (!writeDataSourcePoolId.equals(key) && readDataSourcePoolIds.indexOf(key) == -1) {
                            synchronized (readDataSourcePoolIds) {
                                readDataSourcePoolIds.offerLast(key);
                                readDataSourcePoolIds.notifyAll();
                            }
                        }
                    } catch (IOException e) {
                        if (writeDataSourcePoolId.equals(key)) {
                            //启用备份数据库
                        } else {
                            synchronized (readDataSourcePoolIds) {
                                readDataSourcePoolIds.remove(key);
                                readDataSourcePoolIds.notifyAll();
                            }
                        }
                    }
                }
            }
        }, 1000L, CHECK_TIME);
    }

    private static class SingletonDSPoolFactory {
        private static DSPoolFactory INSTANCE = new DSPoolFactory();
    }

    public static DSPoolFactory getInstance() {
        return SingletonDSPoolFactory.INSTANCE;
    }

    private DSPoolFactory() {
    }

    public String getMasterPoolId() {
        return writeDataSourcePoolId;
    }

    public String getSlavePoolId() {
        synchronized (readDataSourcePoolIds) {
            String readPool = readDataSourcePoolIds.pollFirst();
            if (readPool != null) {
                readDataSourcePoolIds.offerLast(readPool);//放到最后面去
                readDataSourcePoolIds.notifyAll();
            } else {
                readPool = writeDataSourcePoolId;
            }
            return readPool;
        }
    }

    public void addDataSource(String poolId, String address) {
        synchronized (readDataSourcePoolIds) {
            readDataSourcePoolIds.offerLast(poolId);
            readDataSourcePoolIds.notifyAll();
        }
        synchronized (dataSourceAddresses) {
            dataSourceAddresses.put(poolId, address);
        }
    }

    /**
     * 主要解决程序不停止运行时移除机器
     *
     * @param poolId
     */
    public void removeDataSource(String poolId) {
        synchronized (readDataSourcePoolIds) {
            readDataSourcePoolIds.remove(poolId);
            readDataSourcePoolIds.notifyAll();
        }
        synchronized (dataSourceAddresses) {
            dataSourceAddresses.remove(poolId);
        }
    }

    public List<String> getAllSlavePoolId() {
        List<String> poolIdList = new ArrayList<String>(readDataSourcePoolIds.size());
        Iterator<String> iterator = readDataSourcePoolIds.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            poolIdList.add(key);
        }

        return poolIdList;
    }

    public void stopTimer() {
        timer.cancel();
    }
}