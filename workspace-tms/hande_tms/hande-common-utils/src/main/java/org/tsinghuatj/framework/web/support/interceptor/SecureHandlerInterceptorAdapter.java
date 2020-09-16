package org.tsinghuatj.framework.web.support.interceptor;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.utils.SecurityUtils;
import org.tsinghuatj.framework.web.support.annotation.Secure;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yanghongliang
 * @category 权限验证
 * @version 2017-10-31 初始化
 */
public class SecureHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
	private ObjectMapper objectMapper = new ObjectMapper();
	   
   // @Autowired private StringRedisTemplate template;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		super.preHandle(request, response, handler);
		if (handler instanceof HandlerMethod) {
			// 获取方法注解，根据注解的类型
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Secure secure = handlerMethod.getMethodAnnotation(Secure.class);
			if (secure != null) {
				if (SecurityUtils.isAuthenticated() && SecurityUtils.hasAllAuthority(secure.has()) && SecurityUtils.hasAnyAuthority(secure.any()) && SecurityUtils.notAuthority(secure.not())) {
					return true;
				}
				if (!SecurityUtils.isAuthenticated()) {
					// 没登录
					try (Writer writer = response.getWriter();) {
						response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
						response.setStatus(HttpServletResponse.SC_OK);
						objectMapper.writeValue(writer, new AjaxReturn(501, null, null));
					}
				} else {
					// 没权限
					try (Writer writer = response.getWriter();) {
						response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
						response.setStatus(HttpServletResponse.SC_OK);
						objectMapper.writeValue(writer, new AjaxReturn(401, null, null));
					}
				}
				return false;
			}
		}
		return true;
	}
}
