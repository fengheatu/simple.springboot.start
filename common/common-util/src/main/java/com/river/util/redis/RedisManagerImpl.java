package com.river.util.redis;

import com.river.util.exception.ServiceException;
import com.river.util.res.ResCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * create by river  2018/5/15
 * desc:
 *
 * @author river
 */
@Service
public class RedisManagerImpl implements RedisManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisManagerImpl.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean expire(String key, long time) {
        logger.info("指定缓存失效时间：key={},time={} s", key, time);
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.expire(key, -1, TimeUnit.SECONDS);
            }
            return false;
        } catch (Exception e) {
            logger.error("指定缓存失效时间异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 获取key过期时间
     *
     * @param key
     * @return
     */
    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param keys
     */
    @Override
    public void del(List<String> keys) {
        redisTemplate.delete(keys);

    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public boolean set(String key, String value) {
        logger.info("普通缓存放入key={},value={}", key, value);
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("普通缓存放入异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public boolean set(String key, String value, long time) {
        logger.info("普通缓存放入并设置时间key=【" + key + "】 value=【" + value + "】 time=【" + time + "】 s");
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("普通缓存放入并设置时间异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }


    /**
     * 递增
     *
     * @param key   键
     * @param delta
     * @return
     */
    @Override
    public long incr(String key, long delta) {
        logger.info("递增key=[{}] delta=[{}]", key, delta);
        if (delta < 0) {
            throw new ServiceException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta
     * @return
     */
    @Override
    public long decr(String key, long delta) {
        logger.info("递减key=[{}] delta=[{}]", key, delta);
        if (delta < 0) {
            throw new ServiceException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    @Override
    public String hget(String key, String item) {
        return (String) redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    @Override
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    @Override
    public boolean hmset(String key, Map<String, String> map) {
        logger.info("HashSet  key=" + key + " map=" + map);
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error("HashSet异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    @Override
    public boolean hmset(String key, Map<String, String> map, long time) {
        logger.info("HashSet 并设置时间  key=" + key + " map=" + map + "time=" + time + " s");
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("HashSet 并设置时间异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    @Override
    public boolean hset(String key, String item, String value) {
        logger.info("一张hash表中放入数据,如果不存在将创建 key=" + key + " item=" + item + " value=" + value);
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            logger.error(" 向一张hash表中放入数据,如果不存在将创建异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    @Override
    public boolean hset(String key, String item, String value, long time) {
        logger.info("一张hash表中放入数据,设置时间 key=" + key + " item=" + item + " value=" + value + " time=" + time + " s");
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("一张hash表中放入数据,设置时间异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    @Override
    public void hdel(String key, String... item) {
        logger.info("删除hash表中的值 key=" + key + "item=" + item);
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    @Override
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key,item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    @Override
    public double hincr(String key, String item, double by) {
        logger.info("hash递增 key=" + key + " item=" +item + " by=" + by);
        try {
            if(by < 0) {
                throw new ServiceException("递增因子必须大于0");
            }
            return redisTemplate.opsForHash().increment(key,item,by);
        } catch (Exception e) {
            logger.error("hash递增异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    @Override
    public double hdecr(String key, String item, double by) {
        logger.info("hash递减 key=" + key + " item=" +item + " by=" + by);
        try {
            if(by < 0) {
                throw new ServiceException("递减因子必须大于0");
            }
            return redisTemplate.opsForHash().increment(key,item,-by);
        } catch (Exception e) {
            logger.error("hash递减异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    @Override
    public Set<String> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    @Override
    public boolean sHasKey(String key, String value) {
        return redisTemplate.opsForSet().isMember(key,value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSet(String key, String... values) {
        logger.info("将数据放入set缓存 key=" + key +" value=" + values);
        try {
            return redisTemplate.opsForSet().add(key,values);
        } catch (Exception e) {
            logger.error("将数据放入set缓存异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSetAndTime(String key, long time, String... values) {
        logger.info("将数据放入set缓存 key=" + key +" value=" + values + " time=" + time + " s");
        try {
            long count =  redisTemplate.opsForSet().add(key,values);
            if(time > 0) {
                expire(key,time);
            }
            return count;
        } catch (Exception e) {
            logger.error("将数据放入set缓存异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    @Override
    public long sGetSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @Override
    public long setRemove(String key, String... values) {
        logger.info("移除值为value的 key=" + key +" value=" + values);
        try {
            return   redisTemplate.opsForSet().remove(key,values);
        } catch (Exception e) {
            logger.error("移除值为value的异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    @Override
    public List<String> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    @Override
    public long lGetListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    @Override
    public String lGetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key,index);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean lSet(String key, String value) {
        logger.info("将list放入缓存 key=" + key +" value=" + value);
        try {
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        } catch (Exception e) {
            logger.error("将list放入缓存异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, String value, long time) {
        logger.info("将list放入缓存  key=" + key +" value=" + value +" time= " +time+ " s" );
        try {
            redisTemplate.opsForList().rightPush(key,value);
            if(time > 0) {
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            logger.error("将list放入缓存异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean lSet(String key, List<String> value) {
        logger.info("将list放入缓存  key=" + key +" value=" + value);
        try {
            redisTemplate.opsForList().rightPushAll(key,value);
            return true;
        } catch (Exception e) {
            logger.error("将list放入缓存异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, List<String> value, long time) {
        logger.info("将list放入缓存  key=" + key +" value=" + value +" time= " +time+ " s" );
        try {
            redisTemplate.opsForList().rightPushAll(key,value);
            if(time > 0) {
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            logger.error("将list放入缓存异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    @Override
    public boolean lUpdateIndex(String key, long index, String value) {
        logger.info("根据索引修改list中的某条数据  key=" + key +" value=" + value +" index= " +index);
        try {
            redisTemplate.opsForList().set(key,index,value);
            return true;
        } catch (Exception e) {
            logger.error("根据索引修改list中的某条数据异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public long lRemove(String key, long count, String value) {
        logger.info("移除N个值为value  key=" + key +" value=" + value +" count= " +count);
        try {
            Long remove = redisTemplate.opsForList().remove(key,count,value);
            return remove;
        } catch (Exception e) {
            logger.error("移除N个值为value异常", e);
            throw new ServiceException(ResCodeEnum.sys_error);
        }
    }
}
