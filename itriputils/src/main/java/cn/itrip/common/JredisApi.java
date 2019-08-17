package cn.itrip.common;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class JredisApi {
    Jedis jedis=new Jedis("127.0.0.1",6379,60*60*2);
    public  void  SetRedis(String key,String value,int date){
        jedis.auth("123456");
        jedis.setex(key,date,value);
    }
    public String getRedis(String key){
        jedis.auth("123456");
        return jedis.get(key);

    }
}
