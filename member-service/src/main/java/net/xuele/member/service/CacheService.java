package net.xuele.member.service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务
 */
public interface CacheService {
    /**
     * 获取缓存数据
     *
     * @param key key值
     * @param <V> 类型
     * @return 缓存数据
     */
    <V> V get(String key);

    /**
     * 存储缓存数据
     *
     * @param key     key
     * @param value   要存储的数据
     * @param timeout 存储时间
     * @param unit    存储时间类型
     * @return 1：成功；2：失败
     */
    int set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 删除一个缓存数据
     *
     * @param key key值
     * @return 1：成功；2：失败
     */
    int delete(String key);

    /**
     * 删除多个缓存数据
     *
     * @param keys keys值
     * @return 1：成功；2：失败
     */
    int delete(Collection<String> keys);
}
