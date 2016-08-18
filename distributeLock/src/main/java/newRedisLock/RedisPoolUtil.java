package newRedisLock;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User：chencheng
 * Date:2016/8/12
 * Time:17:09
 * To change this template use File | Settings | File Templates.
 */
public class RedisPoolUtil {
    private ShardedJedisPool jedisPool;
    private volatile static RedisPoolUtil redisPool;

    private RedisPoolUtil() {
    }

    public static RedisPoolUtil getRedisPoolSinleton() {
        if (redisPool == null) {
            synchronized (RedisPoolUtil.class) {
                if (redisPool == null)
                    redisPool = new RedisPoolUtil();
            }
        }
        return redisPool;
    }

    public ShardedJedisPool getJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(RedisPoolConfig.MAX_TOTAL));
        poolConfig.setMaxIdle(Integer.parseInt(RedisPoolConfig.MAX_IDEL));
        poolConfig.setMaxWaitMillis(Integer.parseInt(RedisPoolConfig.MAX_WAIT_MILLIS));
        poolConfig.setTestOnBorrow(Boolean.parseBoolean(RedisPoolConfig.TESTNBORROW));
        poolConfig.setTestOnReturn(Boolean.parseBoolean(RedisPoolConfig.TESTONRETURN));
        List<Map> hosts = RedisPoolConfig.HOSTLIST;
        JedisShardInfo[] shardInfos = new JedisShardInfo[hosts.size()];
        for (int i = 0; i < hosts.size(); i++) {
            shardInfos[i] = new JedisShardInfo(hosts.get(i).get("ip").toString(),
                    Integer.parseInt(hosts.get(i).get("port").toString()), 5000);
        }
        List<JedisShardInfo> infoList = Arrays.asList(shardInfos);
        jedisPool = new ShardedJedisPool(poolConfig, infoList);
        return jedisPool;
    }


}
