package com.ctd.util.database.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.util.tools.ReadWriteProperties;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hzm on 2014/8/8.
 */
public class MongoBdUtil {
    private static List<ServerAddress> addresses = null;
    private static MongoClient mongoClient = null;
    private static DB db;
    private static MongoBdUtil instance = new MongoBdUtil();

    public static MongoBdUtil getInstance() {
        return instance;
    }

    private MongoBdUtil() {
    }

    static {
        try {
            ReadWriteProperties propertiesUtil = ReadWriteProperties.getInstance();
            Set<Map.Entry<Object, Object>> set = propertiesUtil.readAllValue("mongoServers.properties");//mongos服务器
            if (set != null) {
                addresses = new ArrayList<ServerAddress>(set.size());
                Map.Entry<Object, Object> entry = null;
                Object value = null;
                String[] server = null;
                Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    server = value.toString().split(":");
                    if (server == null || server.length < 2) {
                        continue;
                    }
                    ServerAddress serverAddress = new ServerAddress(server[0], Integer.parseInt(server[1]));
                    addresses.add(serverAddress);
                }
                MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
                //控制系统在发生连接错误时是否重试 ，默以为false --boolean
                builder.autoConnectRetry(true);
                //每个主机的连接池大小，当连接池被用光时，会被阻塞住 ，默以为10 --int
                builder.connectionsPerHost(1000);
                //multiplier for connectionsPerHost for # of threads that can block if connectionsPerHost is 10,
                // and threadsAllowedToBlockForConnectionMultiplier is 5, then 50 threads can block more than that and an exception will be throw --int
                builder.threadsAllowedToBlockForConnectionMultiplier(500);
                //被阻塞线程从连接池获取连接的最长等待时间（ms） --int
                builder.maxWaitTime(5000);
                //在建立（打开）套接字连接时的超时时间（ms），默以为0（无穷） --int
                builder.connectTimeout(15000);
                //套接字超时时间;该值会被传递给Socket.setSoTimeout(int)。默以为0（无穷） --int
                builder.socketTimeout(0);
                MongoClientOptions options = builder.build();
                mongoClient = new MongoClient(addresses, options);
                /**
                 * primary:默认参数，只从主节点上进行读取操作；
                 * primaryPreferred:大部分从主节点上读取数据,只有主节点不可用时从secondary节点读取数据。
                 * secondary:只从secondary节点上进行读取操作，存在的问题是secondary节点的数据会比primary节点数据“旧”。
                 * secondaryPreferred:优先从secondary节点进行读取操作，secondary节点不可用时从主节点读取数据；
                 * nearest:不管是主节点、secondary节点，从网络延迟最低的节点上读取数据。
                 */
                ReadPreference readPreference = ReadPreference.secondaryPreferred();
                mongoClient.setReadPreference(readPreference);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库，同时提供切换数据库
     *
     * @param dbName
     */
    public void getDB(String dbName) {
        if (db == null || !dbName.equals(db.getName())) {
            db = mongoClient.getDB(dbName);
        }
    }

    /**
     * 获取集合（表）
     *
     * @param collection
     * @return
     */
    public DBCollection getCollection(String collection) {
        return db.getCollection(collection);
    }

    /**
     * 插入
     *
     * @param collection
     * @param map
     */
    public void insert(String collection, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        DBObject dbObject = map2Object(map);
        getCollection(collection).insert(dbObject);
    }

    /**
     * 插入
     *
     * @param collection
     * @param dbObject
     */
    public void insertDb(String collection, DBObject dbObject) {
        if (dbObject == null) {
            return;
        }
        getCollection(collection).insert(dbObject);
    }

    /**
     * 批量插入
     *
     * @param collection
     * @param list
     */
    public void insertBatch(String collection, List<Map<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<DBObject> listDB = new ArrayList<DBObject>(list.size());
        Iterator<Map<String, Object>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            DBObject dbObject = map2Object(map);
            listDB.add(dbObject);
        }
        getCollection(collection).insert(listDB);
    }

    /**
     * 批量插入
     *
     * @param collection
     * @param list
     */
    public void insertBatchDb(String collection, List<DBObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        getCollection(collection).insert(list);
    }

    /**
     * 更新
     *
     * @param collection
     * @param setFields
     * @param whereFields
     */
    public void update(String collection, Map<String, Object> setFields, Map<String, Object> whereFields) {
        DBObject obj1 = map2Object(setFields);
        DBObject obj2 = map2Object(whereFields);
        getCollection(collection).updateMulti(obj1, obj2);
    }

    /**
     * 更新
     *
     * @param collection
     * @param setFields
     * @param whereFields
     */
    public void updateDb(String collection, DBObject setFields, DBObject whereFields) {
        getCollection(collection).updateMulti(setFields, whereFields);
    }

    /**
     * 删除
     *
     * @param collection
     * @param map
     */
    public void delete(String collection, Map<String, Object> map) {
        DBObject obj = map2Object(map);
        getCollection(collection).remove(obj);
    }

    /**
     * 删除
     *
     * @param collection
     * @param deleteFields
     */
    public void deleteDb(String collection, DBObject deleteFields) {
        getCollection(collection).remove(deleteFields);
    }

    /**
     * 批量删除
     *
     * @param collection
     * @param list
     */
    public void deleteBatch(String collection, List<Map<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        DBCollection dbCollection = getCollection(collection);
        Map<String, Object> map = null;
        DBObject dbObject = null;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            if (map == null || map.isEmpty()) {
                continue;
            }
            dbObject = map2Object(list.get(i));
            dbCollection.remove(dbObject);
        }
    }

    /**
     * 批量删除
     *
     * @param collection
     * @param list
     */
    public void deleteBatchDb(String collection, List<DBObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        DBCollection dbCollection = getCollection(collection);
        DBObject dbObject = null;
        for (int i = 0; i < list.size(); i++) {
            dbObject = list.get(i);
            if (dbObject == null) {
                continue;
            }
            dbCollection.remove(dbObject);
        }
    }

    /**
     * 删除全部
     *
     * @param collection
     */
    public void deleteAll(String collection) {
        List<DBObject> rs = findAll(collection);
        if (rs == null || rs.isEmpty()) {
            return;
        }
        DBCollection dbCollection = getCollection(collection);
        for (int i = 0; i < rs.size(); i++) {
            dbCollection.remove(rs.get(i));
        }
    }

    /**
     * 计算满足条件条数
     *
     * @param collection
     * @param map
     * @return
     */
    public long getCont(String collection, Map<String, Object> map) {
        return getCollection(collection).getCount(map2Object(map));
    }

    /**
     * 计算满足条件条数
     *
     * @param collection
     * @param dbObject
     * @return
     */
    public long getContDb(String collection, DBObject dbObject) {
        return getCollection(collection).getCount(dbObject);
    }

    /**
     * 计算集合总条数
     *
     * @param collection
     * @return
     */
    public long getCount(String collection) {
        return getCollection(collection).find().count();
    }

    /**
     * 查找对象（根据主键_id）
     *
     * @param collection
     * @param _id
     * @return
     */
    public DBObject findById(String collection, String _id) {
        DBObject obj = new BasicDBObject();
        obj.put("_id", ObjectId.massageToObjectId(_id));
        return getCollection(collection).findOne(obj);
    }

    /**
     * 查找集合所有对象
     *
     * @param collection
     * @return
     */
    public List<DBObject> findAll(String collection) {
        return getCollection(collection).find().toArray();
    }

    /**
     * 查找（返回一个对象）
     *
     * @param collection
     * @param map
     * @return
     */
    public DBObject findOne(String collection, Map<String, Object> map) {
        DBCollection coll = getCollection(collection);
        return coll.findOne(map2Object(map));
    }

    /**
     * 查找（返回一个对象）
     *
     * @param collection
     * @param dbObject
     * @return
     */
    public DBObject findOneDb(String collection, DBObject dbObject) {
        return getCollection(collection).findOne(dbObject);
    }

    /**
     * 查找（返回一个List<DBObject>
     *
     * @param collection
     * @param map
     * @return
     * @throws Exception
     */
    public List<DBObject> find(String collection, Map<String, Object> map) throws Exception {
        List<DBObject> dbObjectList = null;
        DBCollection coll = getCollection(collection);
        DBCursor c = coll.find(map2Object(map));
        if (c != null) {
            dbObjectList = c.toArray();
            c.close();
        }
        return dbObjectList;
    }

    /**
     * 查找（返回一个List<DBObject>
     *
     * @param collection
     * @param dbObject
     * @return
     * @throws Exception
     */
    public List<DBObject> findDb(String collection, DBObject dbObject) throws Exception {
        List<DBObject> dbObjectList = null;
        DBCollection coll = getCollection(collection);
        DBCursor c = coll.find(dbObject);
        if (c != null) {
            dbObjectList = c.toArray();
            c.close();
        }
        return dbObjectList;
    }

    public void close() {
        if (db != null) {
            db.requestDone();
            db = null;
        }
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    /**
     * map转DBObject
     *
     * @param map
     * @return
     */
    private DBObject map2Object(Map<String, Object> map) {
        DBObject dbObject = new BasicDBObject();
        dbObject.putAll(map);
        return dbObject;
    }
}