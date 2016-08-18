package newRedisLock;

import redis.clients.jedis.ShardedJedis;


/**
 * Description:
 * 算法思路：一共进行三次判断
 *         一：判断需要加锁的对象是否已被加锁，如果没有，则加锁，lock=1，表示加锁成功
 *         二：如果对象已被加锁，则判断锁是否超时
 *         三：当多个进程同时发现锁超时时，为了限制只能有一个进程获得锁
 * User：chencheng
 * Date:2016/8/15
 * Time:14:03
 * To change this template use File | Settings | File Templates.
 */
public class RedisLockUtil implements RedisLock {

    RedisPoolUtil redisPool;

    public RedisLockUtil(RedisPoolUtil redisPool) {
        this.redisPool = redisPool;
    }

    public boolean getLock(String lockname, int locktime) {
        long lock = 0;
        ShardedJedis shardedJedis = redisPool.getJedisPool().getResource();
        try {
            long timestamp = now() + locktime;
            lock = shardedJedis.setnx(lockname, String.valueOf(timestamp));
            if (lock == 1 ||
                    (now() > Long.parseLong(shardedJedis.get(lockname)) &&
                            now() > Long.parseLong(shardedJedis.getSet(lockname, String.valueOf(timestamp)))))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        } finally {
            shardedJedis.close();
        }

    }

    public void unLock(String lockname) {
        ShardedJedis shardedJedis = redisPool.getJedisPool().getResource();
        try {
            if (shardedJedis != null) {
                shardedJedis.del(lockname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }
}
