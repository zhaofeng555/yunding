package com.haojg.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CorsInterceptor implements HandlerInterceptor {

	    @Override
	    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
	    	
	    	httpServletResponse.setContentType("text/html;charset=UTF-8");
			httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
			httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			httpServletResponse.setHeader("Access-Control-Max-Age", "0");
			httpServletResponse.setHeader("Access-Control-Allow-Headers",
					"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
			httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
			httpServletResponse.setHeader("XDomainRequestAllowed", "1");
			
			log.info(httpServletRequest.getRequestURI());
			
	    	return true;
	    }

	    @Override
	    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	    }

	    @Override
	    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	    }
}
