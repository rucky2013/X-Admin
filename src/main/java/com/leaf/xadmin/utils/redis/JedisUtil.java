package com.leaf.xadmin.utils.redis;

import com.alibaba.fastjson.JSON;
import com.leaf.xadmin.constants.GlobalConstants;
import com.leaf.xadmin.utils.serializer.SerializeUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * JedisUtil工具集合
 *
 * @author leaf
 * <p>date: 2018-1-13 17:07:13</p>
 */
public class JedisUtil {
    /**
     * 默认过期时间
     */
    private static final int DEFAULT_EXPIRE_TIME = GlobalConstants.JWT_TOKEN_TIMEOUT * 60;
    /**
     * redis执行成功结果
     */
    private static final String SUCCESS = GlobalConstants.REDIS_RETURN_SUCCESS;

    /**
     * 添加一个字符串值(成功返回1，失败返回0)
     *
     * @param key
     * @param value
     * @return
     */
    public static int set(String key, String value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            if (SUCCESS.equalsIgnoreCase(jedis.set(key, value))) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 缓存一个字符串值(设置默认过期时间)
     *
     * @param key
     * @param value
     * @return
     */
    public static int setExByDefault(String key, String value) {
        return setEx(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 缓存一个字符串值(timeout单位为秒)
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static int setEx(String key, String value, int timeout) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            if (SUCCESS.equalsIgnoreCase(jedis.setex(key, timeout, value))) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 添加一个指定类型的对象
     *
     * @param key
     * @param value
     * @return
     */
    public static <T> int set(String key, T value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            byte[] data = SerializeUtil.Json.serializer(value);
            String result = jedis.set(key.getBytes(), data);
            if (SUCCESS.equalsIgnoreCase(result)) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 缓存一个指定类型的对象
     *
     * @param key
     * @param value
     * @return
     */
    public static <T> int setExByDefault(String key, T value) {
        return setEx(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 缓存一个指定类型的对象
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static <T> int setEx(String key, T value, int timeout) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            byte[] data = SerializeUtil.Json.serializer(value);
            String result = jedis.setex(key.getBytes(), timeout, data);
            if (SUCCESS.equalsIgnoreCase(result)) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 数值+1并返回结果, 若失败返回null
     *
     * @param key
     * @return
     * @throws JedisDataException
     */
    public static Long incr(String key) throws JedisDataException {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            return jedis.incr(key);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 数值-1并返回结果, 若失败返回null
     *
     * @param key
     * @return
     * @throws JedisDataException
     */
    public static Long decr(String key) throws JedisDataException {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            return jedis.decr(key);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 添加字符串值到list中(成功返回1, 失败返回0)
     *
     * @param key
     * @param value
     * @return
     */
    public static int setList(String key, String... value) {
        return setExList(key, -1, value);
    }

    /**
     * 缓存一个字符串值到list中，设置默认失效时间为一个小时
     *
     * @param key
     * @param value
     * @return
     */
    public static int setExListByDefault(String key, String... value) {
        return setExList(key, DEFAULT_EXPIRE_TIME, value);
    }

    /**
     * 缓存字符串值到list中，并设置timeout
     *
     * @param key
     * @param value
     * @return
     */
    public static int setExList(String key, int timeout, String... value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            Long result = jedis.rpush(key, value);
            if (timeout > -1) {
                jedis.expire(key, timeout);
            }
            if (result != null && result != 0) {
                return 1;
            } else {
                return 0;
            }

        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 添加<T>类型对象值到list中
     *
     * @param key
     * @param value
     * @return
     */
    @SafeVarargs
    public static <T> int setList(String key, T... value) {
        return setExList(key, -1, value);
    }

    /**
     * 缓存一个<T>类型对象值到list中
     *
     * @param key
     * @param value
     * @return
     */
    @SafeVarargs
    public static <T> int setExListByDefault(String key, T... value) {
        return setExList(key, DEFAULT_EXPIRE_TIME, value);
    }

    /**
     * 缓存一个<T>类型对象值到list中
     *
     * @param key
     * @param value
     * @return
     */
    @SafeVarargs
    public static <T> int setExList(String key, int timeout, T... value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            int res = 0;
            for (T t : value) {
                byte[] data = SerializeUtil.Json.serializer(t);
                Long result = jedis.rpush(key.getBytes(), data);
                if (result != null && result != 0) {
                    res++;
                }
            }
            if (timeout > -1) {
                jedis.expire(key.getBytes(), timeout);
            }
            if (res != 0) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 添加字符串到set(若key存在就追加，否则增加，成功返回1，失败或未受影响返回0)
     *
     * @param key
     * @param value
     * @return
     */
    public static int setSet(String key, String... value) {
        return setExSet(key, -1, value);
    }

    /**
     * 添加字符串set
     *
     * @param key
     * @param value
     * @return
     */
    public static int setExSetByDefault(String key, String... value) {
        return setExSet(key, DEFAULT_EXPIRE_TIME, value);
    }

    /**
     * 添加字符串set
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static int setExSet(String key, int timeout, String... value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            Long result = jedis.sadd(key, value);
            if (timeout > -1) {
                jedis.expire(key, timeout);
            }
            if (result != null && result != 0) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 添加<T>类型到set集合
     *
     * @param key
     * @param value
     * @return
     */
    @SafeVarargs
    public static <T> int setSet(String key, T... value) {
        return setExSet(key, -1, value);
    }

    /**
     * 缓存一个<T>类型到set集合
     *
     * @param key
     * @param value
     * @return
     */
    @SafeVarargs
    public static <T> int setExSetByDefault(String key, T... value) {
        return setExSet(key, DEFAULT_EXPIRE_TIME, value);
    }

    /**
     * 缓存<T>类型到set集合
     *
     * @param key
     * @param value
     * @return
     */
    @SafeVarargs
    public static <T> int setExSet(String key, int timeout, T... value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            int res = 0;
            for (T t : value) {
                byte[] data = SerializeUtil.Json.serializer(t);
                Long result = jedis.sadd(key.getBytes(), data);
                if (result != null && result != 0) {
                    res++;
                }
            }
            if (timeout > -1) {
                jedis.expire(key.getBytes(), timeout);
            }
            if (res != 0) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 添加一个Map<K, V>集合
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V> int setMap(String key, Map<K, V> value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            String data = JSON.toJSONString(value);
            if (SUCCESS.equalsIgnoreCase(jedis.set(key, data))) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 缓存一个Map<K, V>集合
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V> int setExMapByDefault(String key, Map<K, V> value) {
        return setExMap(key, DEFAULT_EXPIRE_TIME, value);
    }

    /**
     * 缓存一个Map<K, V>集合
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static <K, V> int setExMap(String key, int timeout, Map<K, V> value) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            String data = JSON.toJSONString(value);
            if (SUCCESS.equalsIgnoreCase(jedis.setex(key, timeout, data))) {
                return 1;
            } else {
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获取一个字符串值
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            return jedis.get(key);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得一个指定类型的对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            byte[] data = jedis.get(key.getBytes());
            T result = null;
            if (data != null) {
                result = SerializeUtil.Json.deserializer(data, clazz);
            }
            return result;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得一个字符串集合(区间以偏移量START和END指定)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<String> getList(String key, long start, long end) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            List<String> result = jedis.lrange(key, start, end);
            return result;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得一个<T>类型的对象集合(区间以偏移量START和END指定)
     *
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    public static <T> List<T> getList(String key, long start, long end, Class<T> clazz) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            List<byte[]> lrange = jedis.lrange(key.getBytes(), start, end);
            List<T> result = null;
            if (lrange != null) {
                for (byte[] data : lrange) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(SerializeUtil.Json.deserializer(data, clazz));
                }
            }
            return result;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得list元素数
     *
     * @return
     */
    public static long getListCount(String key) {
        if (isValueNull(key)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            return jedis.llen(key);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得一个字符串set集合
     *
     * @param key
     * @return
     */
    public static Set<String> getSet(String key) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            Set<String> result = jedis.smembers(key);
            return result;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得一个Set<T>集合
     *
     * @param key
     * @return
     */
    public static <T> Set<T> getSet(String key, Class<T> clazz) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            Set<byte[]> smembers = jedis.smembers(key.getBytes());
            Set<T> result = null;
            if (smembers != null) {
                for (byte[] data : smembers) {
                    if (result == null) {
                        result = new HashSet<>();
                    }
                    result.add(SerializeUtil.Json.deserializer(data, clazz));
                }
            }
            return result;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得Set集合中元素个数
     *
     * @param key
     * @return
     */
    public static long getSetCount(String key) {
        if (isValueNull(key)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            return jedis.scard(key);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 获得一个Map<V, K>的集合
     *
     * @param key
     * @return
     */
    public static <K, V> Map<K, V> getMap(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            String data = jedis.get(key);
            @SuppressWarnings("unchecked")
            Map<K, V> result = (Map<K, V>) JSON.parseObject(data);
            return result;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 移除set集合中的元素
     *
     * @param value
     */
    public static long removeSet(String key, String... value) {
        if (isValueNull(key)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            return jedis.srem(key, value);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 删除值
     *
     * @param key
     */
    @SafeVarargs
    public static void del(String... key) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            for (int i = 0; i < key.length; i++) {
                jedis.del(key);
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 刷新数据库
     */
    public static void flushDB() {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            jedis.flushDB();
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    /**
     * 检查值是否为null
     *
     * @param obj
     * @return
     */
    private static boolean isValueNull(Object... obj) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null || "".equals(obj[i])) {
                return true;
            }
        }
        return false;
    }

    public static class JedisPoolUtil {

        private static final String PROPERTIES_PATH = "redis/redis.properties";
        private static JedisPool jedisPool;

        static {
            try {
                init();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 初始化Jedis连接池
         *
         * @throws IOException
         */
        private static void init() throws IOException {
            URL resource = JedisPoolUtil.class.getClassLoader().getResource(PROPERTIES_PATH);
            if (resource == null) {
                throw new FileNotFoundException("No found properties file : " + PROPERTIES_PATH);
            }
            // 加载配置文件
            InputStream input = new FileInputStream(resource.getFile());
            Properties p = new Properties();
            p.load(input);

            // 配置JedisPool基础信息
            String host = p.getProperty("redis.host") == null ? "localhost" : p.getProperty("redis.host");
            int port = p.getProperty("redis.port") == null ? 6379 : Integer.parseInt(p.getProperty("redis.port"));
            String password = p.getProperty("redis.password");
            int timeout = p.getProperty("redis.timeout") == null ? 2000 : Integer.parseInt(p.getProperty("redis.timeout"));

            // 判断是否使用默认配置
            boolean isSetDefault = p.getProperty("redis.pool.defaultSetting") != null || Boolean.parseBoolean(p.getProperty("redis.pool.defaultSetting"));
            if (isSetDefault) {
                jedisPool = new JedisPool(new GenericObjectPoolConfig(), host, port, timeout, password);
            } else {
                JedisPoolConfig config = new JedisPoolConfig();
                String blockWhenExhausted = p.getProperty("redis.blockWhenExhausted");
                if (blockWhenExhausted != null) {
                    config.setBlockWhenExhausted(Boolean.parseBoolean(blockWhenExhausted));
                }
                String evictionPolicyClassName = p.getProperty("redis.evictionPolicyClassName");
                if (evictionPolicyClassName != null) {
                    config.setEvictionPolicyClassName(evictionPolicyClassName);
                }
                String jmxEnabled = p.getProperty("redis.jmxEnabled");
                if (jmxEnabled != null) {
                    config.setJmxEnabled(Boolean.parseBoolean(jmxEnabled));
                }
                String lifo = p.getProperty("redis.lifo");
                if (lifo != null) {
                    config.setLifo(Boolean.parseBoolean(lifo));
                }
                String maxIdle = p.getProperty("redis.maxIdle");
                if (maxIdle != null) {
                    config.setMaxIdle(Integer.parseInt(maxIdle));
                }
                String maxTotal = p.getProperty("redis.maxTotal");
                if (maxTotal != null) {
                    config.setMaxTotal(Integer.parseInt(maxTotal));
                }
                String maxWaitMillis = p.getProperty("redis.maxWaitMillis");
                if (maxWaitMillis != null) {
                    config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
                }
                String minEvictableIdleTimeMillis = p.getProperty("redis.minEvictableIdleTimeMillis");
                if (minEvictableIdleTimeMillis != null) {
                    config.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
                }
                String minIdle = p.getProperty("redis.minIdle");
                if (minIdle != null) {
                    config.setMinIdle(Integer.parseInt(minIdle));
                }
                String numTestsPerEvictionRun = p.getProperty("redis.numTestsPerEvictionRun");
                if (numTestsPerEvictionRun != null) {
                    config.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun));
                }
                String softMinEvictableIdleTimeMillis = p.getProperty("redis.softMinEvictableIdleTimeMillis");
                if (softMinEvictableIdleTimeMillis != null) {
                    config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(softMinEvictableIdleTimeMillis));
                }
                String testOnBorrow = p.getProperty("redis.testOnBorrow");
                if (testOnBorrow != null) {
                    config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
                }
                String testWhileIdle = p.getProperty("redis.testWhileIdle");
                if (testWhileIdle != null) {
                    config.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
                }
                String timeBetweenEvictionRunsMillis = p.getProperty("redis.timeBetweenEvictionRunsMillis");
                if (timeBetweenEvictionRunsMillis != null) {
                    config.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
                }
                jedisPool = new JedisPool(config, host, port, timeout, password);
            }

        }

        /**
         * 获取JedisPool中连接对象
         *
         * @return
         */
        public static Jedis getJedis() {
            return jedisPool.getResource();
        }

        public static void closeJedis(Jedis jedis) {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

}
