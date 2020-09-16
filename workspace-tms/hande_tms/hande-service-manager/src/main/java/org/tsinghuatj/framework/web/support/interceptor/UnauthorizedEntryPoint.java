package org.tsinghuatj.framework.web.support.interceptor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.tsinghuatj.framework.domain.AjaxReturn;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 未登录或者用户名密码错误
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws IOException, ServletException {
		String error = authException.getMessage();
		try (Writer writer = response.getWriter();) {
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			objectMapper.writeValue(writer, new AjaxReturn(501, error, null));
		}
	}

}
