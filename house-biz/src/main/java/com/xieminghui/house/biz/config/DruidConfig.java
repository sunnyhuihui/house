package com.xieminghui.house.biz.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ming
 * @create 2018-06-01 下午2:01
 **/
@Configuration
public class DruidConfig {


    //表示spring.druid 开头的配置可以写到yml里面 ，能让他认识
    // 使得我们的数据能够与阿里巴巴连接池绑定
    @ConfigurationProperties(prefix = "spring.druid")
    @Bean(initMethod="init",destroyMethod ="close")//返回阿里巴巴连接池对象
    public DruidDataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        //把下面慢日志的过滤器加入到连接池里面
        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return druidDataSource;
    }


    //阿里巴巴的 慢日志过滤器
    @Bean
    public Filter statFilter(){
        StatFilter statFilter = new StatFilter();
        //给 3 秒的时间 可以现实mysql的 sql ,就是3秒内的sql查询 都不会到慢日志里面，
        statFilter.setSlowSqlMillis(3000);
        //是否打印慢日志sql
        statFilter.setLogSlowSql(true);
        //是否要把日志合并起来
        statFilter.setMergeSql(true);
        return statFilter;
    }


    //druid 监控
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }
}
