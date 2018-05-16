package com.haojg.shouji.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.haojg.shouji.bean.User;
import com.haojg.shouji.controller.UserControlelr;
import com.haojg.shouji.util.UserHelper;

@WebFilter(urlPatterns = "/*", filterName = "indexFilter")
public class SecurityFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	
	private static Set<String> exSecurity = new HashSet<String>();

	private static final Properties props = new Properties();
	static {
		Resource application = new ClassPathResource("application.properties");
		try {
			props.load(application.getInputStream());
			String excludeUrl = props.getProperty("excludeUrl");
			if (StringUtils.isNotBlank(excludeUrl)) {
				String exUrlArr[] = excludeUrl.split(";");
				exSecurity.addAll(Arrays.asList(exUrlArr));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Autowired
//	RedisService redisService;
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (UserHelper.getCurrentUser(request) == null)// 判断session里是否有用户信息
		{
			if (request.getHeader("x-requested-with") != null
					&& request.getHeader("x-requested-with").equalsIgnoreCase(
							"XMLHttpRequest"))// 如果是ajax请求响应头会有，x-requested-with；
			{
				response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
				return false;
			}
		}
		
		return true;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		User user = UserHelper.getCurrentUser(request);

		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();

		logger.info("uri = "+uri);
		
		// 特殊处理的url
		for (String ex : exSecurity) {
			if (StringUtils.contains(uri, ex)) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
		}

		// 未登录时，跳转登录页面
		if (user == null) {

			if (request.getHeader("x-requested-with") != null
					&& request.getHeader("x-requested-with").equalsIgnoreCase(
							"XMLHttpRequest"))// 如果是ajax请求响应头会有，x-requested-with；
			{
				response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
			} else {
				response.sendRedirect(contextPath + "/login");
			}
			return;
		}
		
		/*
		//检查用户唯一地点登录
		String userId = String.valueOf(user.getId());
		String userSessionKey =GlobalConst.USER_LOGIN_SESSION_KEY.concat(userId); 
		if(redisService.exists(userSessionKey)){
			String userLoginSessionId = redisService.get(userSessionKey);
			
			String curSessionId = request.getSession().getId();
			if(StringUtils.equals(userLoginSessionId, curSessionId)){
				
				redisService.expire(userSessionKey, 15*60);//更新过期时间15分
				
			}else{
				if (request.getHeader("x-requested-with") != null
						&& request.getHeader("x-requested-with").equalsIgnoreCase(
								"XMLHttpRequest"))// 如果是ajax请求响应头会有，x-requested-with；
				{
					response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
				} else {
					response.sendRedirect(contextPath + "/jxzc/login.jsp?error=5");
				}
				return;
			}
		}
		*/
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				fConfig.getServletContext());
	}

}
