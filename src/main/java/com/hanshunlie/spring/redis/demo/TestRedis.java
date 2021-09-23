package com.hanshunlie.spring.redis.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestRedis {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void testRedis(){
//        stringRedisTemplate.opsForValue().set("hello1","china ");
//        System.out.println(stringRedisTemplate.opsForValue().get("hello1"));
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//        connection.set("hello02".getBytes(),"shunlie".shunliegetBytes());
//        System.out.println(new String(connection.get("hello02".getBytes())));

        RedisConnection connection1 = stringRedisTemplate.getConnectionFactory().getConnection();
        connection1.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] bytes) {
                byte[] body = message.getBody();
                System.out.println(new String(body));
            }
        }, "ooxx".getBytes());


        while(true){
            stringRedisTemplate.convertAndSend("ooxx","23456789");
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
