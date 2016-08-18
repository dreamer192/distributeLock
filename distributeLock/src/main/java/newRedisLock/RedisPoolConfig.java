package newRedisLock;

import java.io.InputStream;
import java.util.*;

/**
 * Description:
 * User：chencheng
 * Date:2016/8/12
 * Time:17:15
 * To change this template use File | Settings | File Templates.
 */
public class RedisPoolConfig {
    static String REDIS_DOMAIN;
    static String MAX_TOTAL;
    static String MAX_IDEL;
    static String MAX_WAIT_MILLIS;
    static String TESTNBORROW;
    static String TESTONRETURN;
    static List<Map> HOSTLIST;
    static {
        InputStream redisConfig = RedisPoolConfig.class.getClassLoader().getResourceAsStream("redis.properties");
        Properties properties = new Properties();
        try {
            properties.load(redisConfig);
            REDIS_DOMAIN = properties.getProperty("host");
            MAX_TOTAL = properties.getProperty("maxTotal");
            MAX_IDEL = properties.getProperty("maxEdel");
            MAX_WAIT_MILLIS = properties.getProperty("MaxWaitMillis");
            TESTNBORROW = properties.getProperty("TestOnBorrow");
            TESTONRETURN = properties.getProperty("TestOnReturn");
            String[] hosts = REDIS_DOMAIN.split(",");
            HOSTLIST = new ArrayList<Map>();
            for (int i=0;i<hosts.length;i++){
                String[] host = hosts[i].split(":");
                Map m = new HashMap();
                m.put("ip",host[0]);
                m.put("port",host[1]);
                HOSTLIST.add(m);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't read 'redis.properties' property from 'redis.properties' file");
        }
    }

}
