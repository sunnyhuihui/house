package com.xieminghui.house.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//这个类可以控制拦截器的顺序
public class WebMvcConf extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthActionInterceptor authActionInterceptor;
	
	@Autowired
	private AuthInterceptor authInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		//这里是不拦截的请求 authInterceptor 第一个拦截器
		 registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
		    registry //这里是拦截的请求 authActionInterceptor 第二个拦截器
		        .addInterceptor(authActionInterceptor).addPathPatterns("/house/toAdd")
		        .addPathPatterns("/accounts/profile").addPathPatterns("/accounts/profileSubmit")
		        .addPathPatterns("/house/bookmarked").addPathPatterns("/house/del")
		        .addPathPatterns("/house/ownlist").addPathPatterns("/house/add")
		        .addPathPatterns("/house/toAdd").addPathPatterns("/agency/agentMsg")
		        .addPathPatterns("/comment/leaveComment").addPathPatterns("/comment/leaveBlogComment");
		    super.addInterceptors(registry);
	}

	
}
