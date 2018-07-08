package com.xieminghui.house.web.interceptor;

import com.google.common.base.Joiner;
import com.xieminghui.house.common.constants.CommonConstants;
import com.xieminghui.house.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
//鉴权拦截器的实现
public class AuthInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Map<String, String[]> map = request.getParameterMap();
		map.forEach((k,v) -> {
			if (k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")) {
				request.setAttribute(k, Joiner.on(",").join(v));
			}
		});
	    String reqUri =	request.getRequestURI();
	    if (reqUri.startsWith("/static") || reqUri.startsWith("/error") ) {
			return true;
		}
	    HttpSession session = request.getSession(true);
	    User user =  (User)session.getAttribute(CommonConstants.USER_ATTRIBUTE);
	    if (user != null) {
			UserContext.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserContext.remove();
	}
	

}
