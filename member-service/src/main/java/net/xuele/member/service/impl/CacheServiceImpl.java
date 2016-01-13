package net.xuele.member.service.impl;

import net.xuele.member.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/1/4 0004.
 */
public class CacheServiceImpl implements CacheService {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存数据
     *
     * @param key key值
     * @param <V> 类型
     * @return 缓存数据
     */
    @Override
    public <V> V get(String key) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            Object list = operations.get(key);
            if (list != null) {
                return (V) list;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 存储缓存数据
     *
     * @param key     key
     * @param value   要存储的数据
     * @param timeout 存储时间
     * @param unit    存储时间类型
     * @return 1：成功；2：失败
     */
    @Override
    public int set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, timeout, unit);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 删除一个缓存数据
     *
     * @param key key值
     * @return 1：成功；2：失败
     */
    @Override
    public int delete(String key) {
        try {
            redisTemplate.delete(key);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 删除多个缓存数据
     *
     * @param keys keys值
     * @return 1：成功；2：失败
     */
    @Override
    public int delete(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
