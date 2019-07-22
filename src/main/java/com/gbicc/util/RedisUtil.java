//package com.gbicc.util;
//
//import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.geo.Circle;
//import org.springframework.data.geo.Distance;
//import org.springframework.data.geo.GeoResults;
//import org.springframework.data.geo.Point;
//import org.springframework.data.redis.connection.RedisGeoCommands;
//import org.springframework.data.redis.core.GeoOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * redis工具类
// *
// * @author qiankx
// */
//@Component("redisUtil")
//public class RedisUtil {
//
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisTemplate<Object, Object> redisTemplate;
//
//    @Resource(name = "stringRedisTemplate")
//    private ValueOperations<String, String> valOpsStr;
//
//    @Resource(name = "redisTemplate")
//    private ValueOperations<Object, Object> valOps;
//
//    /**
//     * 更新key的有效期
//     */
//    public void stringRedisExpire(String key, Integer values) {
//        if (values != 0) {
//            stringRedisTemplate.expire(key, values, TimeUnit.SECONDS);
//        }
//    }
//
//    /**
//     * 更新key的有效期
//     */
//    public void redisExpire(String key, Integer values) {
//        if (values != 0) {
//
//            redisTemplate.expire(key, values, TimeUnit.SECONDS);
//        }
//    }
//
//    public void setStringAndExpire(String key, String value, Integer seconds) {
//        valOpsStr.set(key, value, seconds, TimeUnit.SECONDS);
//    }
//
//    public void redisTemplateSetAndExpire(String key, Object ob, Integer seconds) {
//        valOps.set(key, ob, seconds, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 缓存字符串值
//     *
//     * @param key   key
//     * @param value 字符串value
//     */
//    public void setStr(String key, String value) {
//        valOpsStr.set(key, value);
//    }
//
//    /**
//     * 缓存Object类型值
//     *
//     * @param key   key
//     * @param value 对象value
//     */
//    public void setObj(String key, Object value) {
//        valOps.set(key, value);
//    }
//
//    public void setObjAndExpire(String key, Object value, Integer seconds) {
//        valOps.set(key, value, seconds, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 根据key获取value
//     *
//     * @param key key
//     * @return value
//     */
//    public String getValuesStr(String key) {
//        String value = (String) valOpsStr.get(key);
//        return value;
//    }
//
//    /**
//     * 根据key获取objList
//     *
//     * @param key key
//     * @return List<T>
//     */
//    public <T> List<T> getObjList(String key, Class<T> clazz) {
//        String value = valOpsStr.get(key);
//        return JSONObject.parseArray(value, clazz);
//    }
//
//    /**
//     * 获取非字符串类型任务
//     *
//     * @param key key
//     * @return value
//     */
//    public Object getValueObj(String key) {
//        Object value = valOps.get(key);
//        return value;
//    }
//
//    /**
//     * 删除key
//     *
//     * @param key 要删除的key
//     */
//    public void deteleKey(String key) {
//        stringRedisTemplate.delete(key);
//    }
//
//    /**
//     * 通过前缀批量删除String类型Key
//     *
//     * @param preKey 删除的key前缀
//     */
//    public void deteleKeyByPre(String preKey) {
//        Set<String> set = stringRedisTemplate.keys(preKey + "*");
//        Iterator<String> it = set.iterator();
//        while (it.hasNext()) {
//            String key = it.next();
//            stringRedisTemplate.delete(key);
//        }
//    }
//
//    /**
//     * 删除key
//     *
//     * @param key 要删除的key
//     */
//    public void deteleObjKey(String key) {
//        redisTemplate.delete(key);
//    }
//
//    /**
//     * 通过前缀批量删除Object类型Key
//     *
//     * @param preKey 删除的key前缀
//     */
//    public void deteleObjKeyByPre(String preKey) {
//        Set<Object> set = redisTemplate.keys(preKey + "*");
//        Iterator<Object> it = set.iterator();
//        while (it.hasNext()) {
//            Object key = it.next();
//            System.out.println(key);
//            redisTemplate.delete(key);
//        }
//    }
//
//    /**
//     * 计数器+1
//     *
//     * @param key 计数器key
//     * @return 返回计数器当前数值
//     */
//    public long increment(String key) {
//        return valOpsStr.increment(key, 1);
//    }
//
//
//    /**
//     * 添加list
//     */
//    public void setObjList(String key, List<Object> list) {
//        for (Object obj : list) {
//            redisTemplate.opsForList().leftPush(key, obj);
//        }
//    }
//
//    public void addToList(String key, Object obj) {
//        redisTemplate.opsForList().leftPush(key, obj);
//
//    }
//
//    /**
//     * 获取集合大小
//     *
//     * @param key list的key
//     * @return list大小
//     */
//    public long getSize(String key) {
//        return redisTemplate.opsForList().size(key);
//    }
//
//    /**
//     * 获得整个集合
//     */
//    public List<Object> getObjList(String key) {
//        return redisTemplate.opsForList().range(key, 0, -1);
//    }
//
//
//    /**
//     * 根据索引获取集合数据
//     */
//    public List<Object> getObjList(String key, int start, int end) {
//        return redisTemplate.opsForList().range(key, start, end);
//    }
//
//    /**
//     * 根据索引获取集合数据
//     */
//    public Set<Object> getObjSet(String key, int start, int end) {
//        return redisTemplate.opsForZSet().range(key, start, end);
//    }
//
//
//    /**
//     * 删除集合顶端元素
//     */
//    public Object lPopList(String key) {
//        return redisTemplate.opsForList().leftPop(key);
//
//    }
//
//    /**
//     * 获取key剩余时间
//     */
//    public Long getExpire(String key, TimeUnit timeUnit) {
//        return redisTemplate.getExpire(key, timeUnit);
//    }
//
//    /**
//     * 删除集合底部元素
//     */
//    public Object rPopList(String key) {
//        return redisTemplate.opsForList().rightPop(key);
//    }
//
//    /**
//     * 在集合的顶部添加元素
//     *
//     * @param key 集合的key
//     */
//    public void lPushList(String key, Object value) {
//        redisTemplate.opsForList().leftPush(key, value);
//    }
//
//    /**
//     * 在集合的底部添加元素
//     *
//     * @param key 集合的key
//     */
//    public void rPushList(String key, Object value) {
//        redisTemplate.opsForList().rightPush(key, value);
//    }
//
//    /**
//     * 添加map对象元素
//     *
//     * @param key map的key
//     */
//    public void putObject(String key, String mapKey, String value) {
//        redisTemplate.opsForHash().put(key, mapKey, value);
//    }
//
//    /**
//     * 获取map对象元素
//     *
//     * @param key    map对应的key
//     * @param mapKey map中的key
//     * @return map中的value
//     */
//    public <T> List<T> getObjectList(String key, String mapKey, Class<T> clz) {
//        String obj = (String) redisTemplate.opsForHash().get(key, mapKey);
//        //String resultStr=JsonUtil.toJson(obj);
//        try {
//            return JSONObject.parseArray(obj, clz);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Map<Object, Object> getEntities(String key) {
//        return redisTemplate.opsForHash().entries(key);
//    }
//
//    /**
//     * 删除map对象元素
//     *
//     * @param key    map对应的key
//     * @param mapKey map中的key
//     */
//    public void deleteObject(String key, String mapKey) {
//        redisTemplate.opsForHash().delete(key, mapKey);
//    }
//
//    public void addToSet(String key, String value) {
//        stringRedisTemplate.opsForSet().add(key, value);
//    }
//
//    public void removeFromSet(String key, String value) {
//        stringRedisTemplate.opsForSet().remove(key, value);
//    }
//
//    public Set<String> getSet(String key) {
//        return stringRedisTemplate.opsForSet().members(key);
//    }
//
//    /**
//     * 根据前缀获取key（慎用，库中key多时效率低）
//     */
//    public Set<String> getKeysByPre(String pre) {
//        return stringRedisTemplate.keys(pre + "*");
//    }
//
//    public boolean ifExist(String key) {
//        return stringRedisTemplate.hasKey(key);
//    }
//
//    public boolean listHasKey(String listkey, String object) {
//        return stringRedisTemplate.opsForList().range(listkey, 0, -1).contains(object);
//    }
//
//    public void lpushList(String key, String value) {
//        stringRedisTemplate.opsForList().leftPush(key, value);
//    }
//
//    public void rpopList(String key) {
//        stringRedisTemplate.opsForList().rightPop(key);
//    }
//
//    public long getListSize(String key) {
//        return stringRedisTemplate.opsForList().size(key);
//    }
//
//
//    /**
//     * 添加坐标点
//     *
//     * @param key
//     * @param point
//     * @param name
//     * @return
//     */
//    public long addGeoPoint(String key, Point point, String name, long time) {
//        try {
//            GeoOperations<Object, Object> geoOperationseo = redisTemplate.opsForGeo();
//            Long aLong = geoOperationseo.geoAdd(key, point, name);
//            if (time > 0) {
//                redisTemplate.expire(key, time, TimeUnit.SECONDS);
//            }
//            return aLong;
//        } catch (Exception e) {
//            LOGGER.error("缓存添加失败 key {} x {} y {} name {} time {}", key, point.getX(), point.getY(), name, time);
//        }
//        return 0;
//    }
//
//
//    /**
//     * 根据指定范围获取周围坐标信息
//     *
//     * @param key
//     * @param point
//     * @param direction
//     * @param distanceUnit
//     * @param distance
//     * @param limit
//     * @return
//     */
//    public GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusGeo(String key, Point point, Sort.Direction direction,
//                                                                      RedisGeoCommands.DistanceUnit distanceUnit, long distance, int limit) {
//
//        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = null;
//        try {
//            GeoOperations<Object, Object> geoOperationseo = redisTemplate.opsForGeo();
//
//            RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
//
//            if (limit>0){
//                geoRadiusCommandArgs.limit(limit);
//            }
//            if (Sort.Direction.ASC.equals(direction)) {
//                geoRadiusCommandArgs.sortAscending();
//            } else {
//                geoRadiusCommandArgs.sortDescending();
//            }
//            geoResults = geoOperationseo.geoRadius(key, new Circle(point, new Distance(distance, distanceUnit)));
//        } catch (Exception e) {
//            LOGGER.error("缓存获取失败 key {} x {} y {} distance {} limit {}", key, point.getX(), point.getY(), distance, limit);
//        }
//        return geoResults;
//    }
//}
