package newRedisLock;

/**
 * Description:
 * User：chencheng
 * Date:2016/8/15
 * Time:14:19
 * To change this template use File | Settings | File Templates.
 */
public interface RedisLock {

    boolean getLock(String lockname, int locktime);

    void unLock(String lockname);

}
