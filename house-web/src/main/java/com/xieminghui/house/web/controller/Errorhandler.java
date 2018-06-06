package com.xieminghui.house.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CountDownLatch;

@ControllerAdvice //controller 辅助类 可以帮忙写异常 如果发生500错误 ，就会记录
public class Errorhandler {
	private static final Logger logger = LoggerFactory.getLogger(Errorhandler.class);

	@ExceptionHandler(value={Exception.class,RuntimeException.class})
	public String error500(HttpServletRequest request,Exception e){
		logger.error(e.getMessage(),e);
		logger.error(request.getRequestURL() + " encounter 500");
		return "error/500";
	}
}

