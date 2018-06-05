package com.xieminghui.house.mooc1.house.autoconfig;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//自己写一个自动加载配置的注解

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HttpClientAutoConfiguration.class)
public @interface EnableHttpClient {

}
