package com.xieminghui.house.biz.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.xieminghui.house.biz.mapper.UserMapper;
import com.xieminghui.house.common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailService {



   @Autowired
   private JavaMailSender mailSender;

   @Autowired
   private UserMapper userMapper;

   @Value("spring.mail.username")
   private String from;


   @Value("domain.name")
   private String domainName; //邮箱ip 服务器ip+端口

   //本地缓存 guava Cache
   //主要用于缓存 key和email   然后最打缓存100个，超过就丢弃，同时100个够大了
   //然后如果超过15分钟还没有注册，就会remove掉这个 缓存
   //同时触发一个操作， 就是把数据库 这个email 删除掉
   private final Cache<String,String> registerCache =
           CacheBuilder.newBuilder().maximumSize(100).
                   expireAfterAccess(15,TimeUnit.MINUTES).
                   removalListener(new RemovalListener<String, String>() {
                      //在过期的时候就会触发一个操作
                      @Override
                      public void onRemoval(RemovalNotification<String, String> removalNotification) {
                         // key - value(email)
                         userMapper.delete(removalNotification.getValue());
                      }
                   }).build();


   private final Cache<String, String> resetCache =  CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES).build();

   public void sentEmail(String title, String url, String email) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(from);
      message.setTo(email);
      message.setText(url);
      mailSender.send(message);
   }


   /**
    * 1 生成一个key ，对应一个email
    * 2 借助springboot mail 发送 邮件
    * 3 借助异步工具进行通知
    * @param email
    */
   @Async
   //这个注解springboot会调用一个线程池，把这个方法丢进线程池，达到异步的效果
   // 而且类型要是public 不能是private
   public void registerNotify(String email) {
      //生成随机的字符串 10位， 然后放在缓存里面
      String randomKey = RandomStringUtils.randomAlphabetic(10);
      registerCache.put(randomKey,email);

      //发送邮件
      String url = "http://"+domainName+"/accounts/verify?key"+randomKey;

      //第一个参数是email的标题， 第二个是链接 ，第三个是用户的email
      sentEmail("房产平台激活邮箱",url,email);
   }

   public boolean enable(String key) {
      String email = registerCache.getIfPresent(key);
      if(StringUtils.isBlank(email)){
         return false;
      }
      User updateUser = new User();
      updateUser.setEmail(email);
      updateUser.setEnable(1);
      userMapper.update(updateUser);
      //手动的把邮箱缓存的key剔除掉
      registerCache.invalidate(key);
      return true;
   }



   /**
    * 发送重置密码邮件
    *
    * @param email
    */
   @Async
   public void resetNotify(String email) {
      String randomKey = RandomStringUtils.randomAlphanumeric(10);
      resetCache.put(randomKey, email);
      String content = "http://" + domainName + "/accounts/reset?key=" + randomKey;
      sentEmail("房产平台密码重置邮件", content, email);
   }

   public String getResetEmail(String key){
      return resetCache.getIfPresent(key);
   }

   public void invalidateRestKey(String key){
      resetCache.invalidate(key);
   }






}
