package newRedisLock;


import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User：chencheng
 * Date:2016/8/15
 * Time:14:12
 * To change this template use File | Settings | File Templates.
 */
public class TestLock {
    static int count = 0;
    public static void main(String[] args) {
        final RedisPoolUtil redisPool = RedisPoolUtil.getRedisPoolSinleton();
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                RedisLockUtil util = new RedisLockUtil(redisPool);
                public void run() {
                    try {
                        while (!util.getLock("lock.foo",300)) {
                            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(100));
                        }
                        count++;
                        System.out.println(Thread.currentThread().getName()+"count="+count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        util.unLock("lock.foo");
                    }
                }
            }).start();
        }
    }
}
