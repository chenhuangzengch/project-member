package net.xuele.member.service.impl;

import net.xuele.member.service.SeqGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * zhihuan.cai 新建于 2015/7/30 0030.
 */

public class SeqGeneratorServiceRedisImpl implements SeqGeneratorService {


    private static final String MEMBER_SEQ_KEY = "global:member:sequence:{0}";

    private Map<String, AtomicLong> indexCache = new ConcurrentHashMap<>();

    private Map<String, Long> seqCache = new ConcurrentHashMap<>();

    private static final int CACHE_STEP = 1000;

    @Autowired
    @Qualifier("signRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    public Long generate(String tableName) {
        String key = tableName.toLowerCase();
        synchronized (key.intern()) {
            AtomicLong index = indexCache.get(key);
            if (index == null || index.longValue() == CACHE_STEP) {
                long value = redisTemplate.opsForValue().increment(MessageFormat.format(MEMBER_SEQ_KEY, key), CACHE_STEP);
                Long seqBase = value - CACHE_STEP;
                seqCache.put(key, seqBase);
                indexCache.put(key, new AtomicLong(1));
                return seqBase + 1;
            } else {
                Long seqBase = seqCache.get(key);
                return seqBase + index.incrementAndGet();
            }
        }
    }


}
