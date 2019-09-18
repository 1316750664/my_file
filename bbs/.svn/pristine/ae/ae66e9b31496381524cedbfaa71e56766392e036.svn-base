package com.ctd.util.database;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.util.tools.CheckTools;
import com.util.tools.ReadWriteProperties;

import java.util.Date;

public class MemCachedUtil {
    private static long DEFAULT_CACHED_TIME = 8 * 60 * 60 * 1000;//单位为秒
    private static String DEFAULT_POOL_NAME = "QSHMEMCACHE";
    private static SockIOPool sockIOPool = null;
    private static MemCachedClient memCachedClient = null;

    private static class SingletonMemCachedUtil {
        private static MemCachedUtil INSTANCE = new MemCachedUtil();
    }

    public static MemCachedUtil getInstance() {
        return SingletonMemCachedUtil.INSTANCE;
    }

    private MemCachedUtil() {
    }

    static {
        if (sockIOPool == null) {
            //设置服务器列表和对应权重
            String[] servers = getServers();
            Integer[] weights = getWeights();
            //获取socket连接池的实例对象
            sockIOPool = SockIOPool.getInstance(DEFAULT_POOL_NAME);
            //将服务器信息设置对连接池
            sockIOPool.setServers(servers);
            sockIOPool.setWeights(weights);
            //设置初始连接数、最小连接数和最大连接数以及最大处理时间
            sockIOPool.setInitConn(5);
            sockIOPool.setMinConn(5);
            sockIOPool.setMaxConn(250);
            sockIOPool.setMaxIdle(6 * 60 * 60 * 1000);//单位为毫秒
            //设置主线程的睡眠时间
            sockIOPool.setMaintSleep(30);//单位为秒
            //设置TCP的参数
            sockIOPool.setNagle(false);//是否使用Nagle算法，默认true
            sockIOPool.setHashingAlg(SockIOPool.CONSISTENT_HASH);
            sockIOPool.setSocketTO(3000);//socket读取等待超时值，单位为毫秒
            sockIOPool.setSocketConnectTO(0);//socket连接等待超时值
            //连接失败恢复开关，设置为TRUE，当宕机的服务器启动或中断的网络连接后，这个socket连接还可继续使用
            sockIOPool.setFailback(true);
            //容错开关，设置为TRUE，当当前socket不可用时，程序会自动查找可用连接并返回，否则返回NULL
            sockIOPool.setFailover(true);//目前只使用单台服务器的情况下先关闭
            //初始化连接池，设置完pool参数后最后调用该方法，启动pool
            sockIOPool.initialize();
        }
        if (memCachedClient == null) {
            memCachedClient = new MemCachedClient(DEFAULT_POOL_NAME);
            //当primitiveAsString为true时使用的编码转化格式，默认值是utf-8，如果确认主要写入数据是中文等非ASCII编码字符，建议采用GBK等更短的编码格式
            memCachedClient.setDefaultEncoding("utf-8");
            //设置cache数据的原始类型存储方式是String，默认值是false，只有在确定cache的数据类型是string的情况下才设为true，这样可以加快处理速度
            memCachedClient.setPrimitiveAsString(true);
            memCachedClient.setSanitizeKeys(false);
        }

    }

    private static String[] getServers() {
        ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
        String serverList = propertiesUtil.readValue("cacheServers", "serverList");
        String defaultCacheTime = propertiesUtil.readValue("cacheServers", "defaultCacheTime");
        if (CheckTools.checkPositiveNum(defaultCacheTime)) {
            DEFAULT_CACHED_TIME = Long.parseLong(defaultCacheTime);
        }
        String[] cacheServers = serverList.split(",");
        int len = cacheServers.length;
        if (len == 0) {
            return new String[]{"127.0.0.1:11211"};
        }
        String lastServer = cacheServers[len - 1];
        if (lastServer == null || "".equals(lastServer.trim())) {
            String[] dest = new String[len - 1];
            System.arraycopy(cacheServers, 0, dest, 0, dest.length);
            return dest;
        }
        return cacheServers;
    }

    private static Integer[] getWeights() {
        ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
        String weightList = propertiesUtil.readValue("cacheServers", "weightList");
        String[] cacheWeights = weightList.split(",");
        int len = cacheWeights.length;
        if (len == 0) {
            return new Integer[]{1};
        }
        Integer[] dest = null;
        String lastWeight = cacheWeights[len - 1];
        if (lastWeight == null || "".equals(lastWeight.trim())) {
            dest = new Integer[len - 1];

        } else {
            dest = new Integer[len];
        }
        for (int i = 0; i < dest.length; i++) {
            dest[i] = Integer.valueOf(cacheWeights[i]);
        }
        return dest;
    }

    /**
     * 仅当存储空间中不存在键相同的数据时才保存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value) {
        return add(key, value, new Date(System.currentTimeMillis() + DEFAULT_CACHED_TIME));
    }

    public boolean add(String key, Object value, Date expiry) {
        if (key == null || "".equals(key)) {
            return false;
        }
        if (memCachedClient.keyExists(key)) {
            return replace(key, value);
        }
        return memCachedClient.add(key, value, expiry);
    }

    /**
     * 仅当存储空间中存在键相同的数据时才保存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean replace(String key, Object value) {
        return memCachedClient.replace(key, value, new Date(System.currentTimeMillis() + DEFAULT_CACHED_TIME));
    }

    public boolean replace(String key, Object value, Date expiry) {
        if (key == null || "".equals(key)) {
            return false;
        }
        if (memCachedClient.keyExists(key)) {
            return memCachedClient.replace(key, value, expiry);
        }
        return add(key, value, expiry);
    }

    /**
     * 与add和replace不同，无论何时都保存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        return set(key, value, new Date(System.currentTimeMillis() + DEFAULT_CACHED_TIME));
    }

    public boolean set(String key, Object value, Date expiry) {
        if (key == null || "".equals(key)) {
            return false;
        }
        return memCachedClient.set(key, value, expiry);
    }

    public Object get(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        return memCachedClient.get(key);
    }

    public boolean delete(String key) {
        if (key == null || "".equals(key)) {
            return false;
        }
        if (memCachedClient.keyExists(key)) {
            return memCachedClient.delete(key);
        } else {
            return true;
        }
    }

    public boolean flushAll() {
        return memCachedClient.flushAll();
    }

    public boolean sync(String key) {
        if (key == null || "".equals(key)) {
            return false;
        }
        if (memCachedClient.keyExists(key)) {
            return memCachedClient.sync(key);
        } else {
            return false;
        }
    }

    public void shutdown() {
        if (sockIOPool != null) {
            sockIOPool.shutDown();
        }
    }
}